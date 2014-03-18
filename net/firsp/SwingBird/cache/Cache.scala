package net.firsp.SwingBird.cache

import scala.collection.mutable.{ListBuffer, HashMap}
import twitter4j.{Status, User}
import net.firsp.SwingBird.lib.UsefulFunc._
import net.firsp.SwingBird.model.StatusModel
import javax.swing.ImageIcon
import java.net.URL
import java.io.{FileOutputStream, BufferedOutputStream, File}
import java.util.concurrent.LinkedBlockingQueue

object Cache {
	val s = new HashMap[Long, StatusModel]
	val u = new HashMap[Long, User]

	def createModel(status: Status): StatusModel = {
		s.getOrElseUpdate(status.getId, {
			val id = status.getId
			val user = status.getUser.getId
			u.put(user, status.getUser)
			val rt = status.isRetweet
			if (rt) createModel(status.getRetweetedStatus)
			val org = if (rt) status.getRetweetedStatus else status
			var text = org.getText
			text = org.getURLEntities.foldLeft(text)((t, e) => t.replace(e.getURL, e.getExpandedURL))
			text = org.getMediaEntities.foldLeft(text)((t, e) => t.replace(e.getURL, e.getExpandedURL))
			val date = status.getCreatedAt
			val via = (if (rt) ("(RT:" + status.getUser.getScreenName + ")") else "") + org.getCreatedAt.toString + " via " + org.getSource.replaceAll("<.+?>", "")
			new StatusModel(id, user, text, date, via, rt, org.getId)
		})
	}

	def getUser(m: StatusModel) = u.get(m.user).get

	def getOriginalUser(m: StatusModel) = getUser(s.get(m.orgId).get)

}

object IconCache {
	val m = new HashMap[String, ImageIcon]
	val d = new ImageIcon("default.png")
	val q = new LinkedBlockingQueue[URL]
	val c = new ListBuffer[Function0[Any]]
	spawn{
		while (true) {
			tryIgnore({
				val u = q.take
				val f = new File("cache/" + u.toString.replaceAll(".*/", ""))
				if (!f.exists) {
					f.getParentFile.mkdirs
					val stream = u.openStream
					val buf = Stream.continually(stream.read).takeWhile(-1 !=).map(_.byteValue).toArray
					val os = new BufferedOutputStream(new FileOutputStream(f))
					os.write(buf)
					os.close
					c.foreach(f => f())
				}
			})
		}
	}

	def addCallback(f: Function0[Any]) = {
		c += f
	}

	def getImage(s: String) = {
		m.getOrElse(s, {
			val f = new File("cache/" + s.replaceAll(".*/", ""))
			if (f.exists) {
				val i = new ImageIcon(f.toString)
				m.put(s, i)
				i
			} else {
				tryIgnore(q.add(new URL(s)))
				d
			}
		})
	}
}


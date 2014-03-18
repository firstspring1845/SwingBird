package net.firsp.SwingBird.cache

import scala.collection.mutable.HashMap
import twitter4j.{Status, User}
import net.firsp.SwingBird.model.StatusModel

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


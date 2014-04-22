package net.firsp.SwingBird.view

import javax.swing._
import javax.swing.table.AbstractTableModel
import java.awt.{Container, Component, Dimension}
import javax.swing.event.TableModelEvent
import scala.collection.mutable.{ArrayBuffer, HashSet}

import twitter4j.Status

import net.firsp.SwingBird.lib.UsefulFunc._
import net.firsp.SwingBird.model.StatusModel
import net.firsp.SwingBird.cache.Cache
import java.awt.event.{KeyAdapter, ActionEvent, KeyEvent}

class TableList extends JTable {
	setTableHeader(null)
	setIntercellSpacing(new Dimension(0, 1))
	setDefaultEditor(classOf[Object], null)

	{
		val im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "selectFirstRow")
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "selectLastRow")
		im.put(KeyStroke.getKeyStroke('j'), "selectNextRow")
		im.put(KeyStroke.getKeyStroke('k'), "selectPreviousRow")
		def act[T](f: (ActionEvent) => T) = new AbstractAction("") {
			override def actionPerformed(e: ActionEvent) = f(e)
		}
		val am = getActionMap
		am.put("home", act((e) => {
			getScrollPane.getVerticalScrollBar.setValue(0)
			scrollRectToVisible(getCellRect(getSelectedRow, 0, false))
		}))
		im.put(KeyStroke.getKeyStroke('H'), "home")
		am.put("end", act((e) => {
			getScrollPane.getVerticalScrollBar.setValue(Integer.MAX_VALUE)
			scrollRectToVisible(getCellRect(getSelectedRow, 0, false))
		}))
		im.put(KeyStroke.getKeyStroke('E'), "end")
	}

	def putAction[T](key: KeyStroke, action: (ActionEvent) => T) = {
		val im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
		val am = getActionMap
		val command = key.toString
		im.put(key, command)
		am.put(command, new AbstractAction(command) {
			override def actionPerformed(e: ActionEvent) = action(e)
		})
	}

	def getScrollPane = {
		getParent match {
			case c: Container => c.getParent match {
				case s: JScrollPane => s
				case _ => new JScrollPane
			}
			case _ => new JScrollPane
		}
	}

	override def removeEditor = {
		getComponents.foreach(_ match {
			case p: CellRendererPane => p
			case c: Component => remove(c)
		})
		super.removeEditor
	}
}

class TimelineView extends TableList {
	val model = new StatusListModel
	setModel(model)

	var lock = false

	putAction(KeyStroke.getKeyStroke('s'), (e) => {
		lock ^= true
	})

	addKeyListener(new KeyAdapter() {
		override def keyTyped(e: KeyEvent) = {
			getValueAt(getSelectedRow, 0) match {
				case s: StatusModel => e.getKeyChar match {
					case 'f' => spawn(getTwitter.createFavorite(s.orgId))
					case 'u' => spawn(getTwitter.destroyFavorite(s.orgId))
					case 'r' => spawn(getTwitter.retweetStatus(s.orgId))
					case _ =>
				}
				case _ =>
			}
		}
		def getTwitter = twitter4j.TwitterFactory.getSingleton
	})

	override def tableChanged(e: TableModelEvent) = {
		super.tableChanged(e)
		tryIgnore(this.prepareRenderer(getDefaultRenderer(classOf[Object]), e.getFirstRow, 0))
		if (getSelectedRow == 0 && e.getFirstRow == 0) {
			changeSelection(1, 0, false, false)
		}
		val v = getScrollPane.getVerticalScrollBar
		if (e.getFirstRow < getSelectedRow) {
			if (lock) {
				v.setValue(v.getValue + getRowHeight(e.getFirstRow))
			}
		}
	}


}

class StatusListModel extends AbstractTableModel {

	val arr = new ArrayBuffer[StatusModel]
	val map = new HashSet[Long]

	def getRowCount: Int = arr.size

	def getColumnCount: Int = 1

	def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = arr(rowIndex)

	def insert(m: StatusModel): Unit = map.synchronized {
		if (!map.contains(m.id)) {
			var index = -1
			if(arr.size > 1) {
				var minIndex = 0
				var maxIndex = arr.size - 1
				while(index == -1 && minIndex != maxIndex - 1) {
					val mid = (minIndex + maxIndex) / 2
					if(m.date.getTime == arr(mid).date.getTime) index = mid
					else if(m.date.getTime > arr(mid).date.getTime) maxIndex = mid
					else minIndex = mid
				}
				if(index == -1) {
					if(m.date.getTime > arr(minIndex).date.getTime) index = minIndex
					else if(m.date.getTime < arr(maxIndex).date.getTime) index = maxIndex + 1
					else index = maxIndex
				}
			}
			else if(arr.size == 1) {
				if(m.date.getTime > arr(0).date.getTime) index = 0 else index = 1
			}
			else index = 0
			invokeAndWait {
				arr.insert(index, m)
				fireTableRowsInserted(index, index)
			}
			map += m.id
		}
	}

	def insert(s: Status): Unit = insert(Cache.createModel(s))
}

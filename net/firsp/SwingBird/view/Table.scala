package net.firsp.SwingBird.view

import javax.swing.{SwingUtilities, CellRendererPane, JScrollPane, JTable}
import javax.swing.table.AbstractTableModel
import java.awt.{Container, Component, Dimension}
import javax.swing.event.TableModelEvent
import scala.collection.mutable.{ArrayBuffer, HashSet}

import twitter4j.Status

import net.firsp.SwingBird.lib.UsefulFunc._
import net.firsp.SwingBird.model.StatusModel
import net.firsp.SwingBird.cache.Cache

class TimelineView extends JTable {
	val model = new ListModel
	setTableHeader(null)
	setIntercellSpacing(new Dimension(0, 1))
	setDefaultEditor(classOf[Object], null)
	setModel(model)

	override def tableChanged(e: TableModelEvent) = {
		super.tableChanged(e)
		if (getSelectedRow == 0) {
			changeSelection(1, 0, false, false)
		}
		val v = getScrollPane.getVerticalScrollBar
		if (e.getFirstRow < getSelectedRow) {
			this.prepareRenderer(getDefaultRenderer(classOf[Object]), e.getFirstRow, 0)
			v.setValue(v.getValue + getRowHeight(e.getFirstRow))
		}
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

class ListModel extends AbstractTableModel {

	val arr = new ArrayBuffer[StatusModel]
	val map = new HashSet[Long]

	def getRowCount: Int = arr.size

	def getColumnCount: Int = 1

	def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = arr(rowIndex)

	def insert(m: StatusModel):Unit = map.synchronized{
		if (!map.contains(m.id)) {
			arr.indexWhere(_.date.getTime <= m.date.getTime) match {
				case -1 => {
					val row = arr.size
					invokeAndWait{
						arr += m
						fireTableRowsInserted(row, row)
					}
				}
				case row: Int => {
					invokeAndWait{
						arr.insert(row, m)
						fireTableRowsInserted(row, row)
					}
				}
			}
			map += m.id
		}
	}

	def insert(s: Status):Unit = insert(Cache.createModel(s))
}

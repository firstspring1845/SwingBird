package net.firsp.SwingBird.view

import javax.swing.event.{TableModelEvent, TableModelListener}
import java.awt.{Container, Component, Color}
import javax.swing.{JPanel, JTable}
import javax.swing.table.TableCellRenderer

class StripedRendererBase(val table: JTable) extends JPanel with TableCellRenderer with TableModelListener {
	var revert = false
	val color = new Color(224, 224, 224)

	override def tableChanged(e: TableModelEvent) = {
		if (e.getFirstRow <= table.getSelectedRow) {
			revert ^= true
		}
	}

	def getColor(row: Int) = if ((row % 2 == 0) ^ revert) Color.white else color

	def setColor(component: Component, color: Color): Unit = {
		component.setBackground(color)
		component match {
			case c: Container => c.getComponents.foreach(setColor(_, color))
			case _ =>
		}
	}

	def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component = {
		setColor(this, getColor(row))
		this
	}
}

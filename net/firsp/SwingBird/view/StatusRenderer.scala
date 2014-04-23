package net.firsp.SwingBird.view

import javax.swing._
import java.awt.{Color, Component}
import net.firsp.SwingBird.model.StatusModel
import net.firsp.SwingBird.cache.{IconCache, Cache}

class StatusRenderer(table: JTable) extends StripedRendererBase(table) {
	val icon = new JLabel
	val name = new JTextArea
	val text = new JTextArea
	val via = new JTextArea

	setLayout(null)
	setOpaque(true)
	Array(name, text, via).foreach((t) => {
		t.setEditable(false)
		t.setLineWrap(true)
	})
	Array(icon, name, text, via).foreach(add)
	icon.setSize(48, 48)
	icon.setLocation(5, 5)

	def setColor(c: Color) = Array(this, icon, name, text, via).foreach(_.setBackground(c))

	override def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component = {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
		val m = value.asInstanceOf[StatusModel]
		val u = Cache.getOriginalUser(m)
		if (false) {
			//fav
			setColor(new Color(255, 204, 51))
		}
		if (m.rt) {
			setColor(new Color(135, 206, 235)) //skyblue
		}
		if (isSelected) {
			setColor(new Color(128, 255, 128))
		}
		icon.setIcon(IconCache.getImage(u.getProfileImageURL))

		val locateX = 48 + 10
		val sizeX = table.getSize().width - locateX
		val max = Integer.MAX_VALUE

		name.setText(u.getScreenName + " / " + u.getName)
		name.setLocation(locateX, 0)
		name.setSize(sizeX, max)
		name.setSize(sizeX, name.getPreferredSize.height)

		text.setText(m.text)
		text.setLocation(locateX, name.getLocation().y + name.getPreferredSize.height)
		text.setSize(sizeX, max)
		text.setSize(sizeX, text.getPreferredSize.height)

		via.setText(m.via)
		via.setLocation(locateX, text.getLocation().y + text.getPreferredSize.height)
		via.setSize(sizeX, max)
		via.setSize(sizeX, via.getPreferredSize.height)

		val height = Math.max(58, name.getPreferredSize.height + text.getPreferredSize.height + via.getPreferredSize.height)
		if (table.getRowHeight(row) != height) {
			table.setRowHeight(row, height)
		}
		this
	}
}


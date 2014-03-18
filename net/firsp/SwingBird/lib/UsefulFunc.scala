package net.firsp.SwingBird.lib

import javax.swing.SwingUtilities
import java.awt.event.{ActionListener, ActionEvent}


object UsefulFunc {
	def invokeLater[T](f: => T) = SwingUtilities.invokeLater(new Runnable(){
		def run = f
	})

	def invokeAndWait[T](f: => T) = SwingUtilities.invokeAndWait(new Runnable(){
		def run = f
	})

	implicit def functionToActionListener[T](f:Function1[ActionEvent,T]) = new ActionListener(){
		def actionPerformed(e:ActionEvent) = f(e)
	}

	def spawn[T](f: => T) = new Thread(new Runnable(){
		def run = f
	}).start

	def tryIgnore[T](f: => T) = {
		try {
			Some(f)
		} catch {
			case _ =>
		}
	}
}


package net.firsp.SwingBird.model

import java.util.Date

class StatusModel(val id: Long, val user: Long, val text: String, val date: Date, val via: String, val rt: Boolean, val orgId: Long)

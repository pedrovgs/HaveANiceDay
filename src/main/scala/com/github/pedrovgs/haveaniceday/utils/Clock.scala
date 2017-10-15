package com.github.pedrovgs.haveaniceday.utils

import javax.inject.Inject

import org.joda.time.DateTime

class Clock @Inject()() {

  val now: DateTime = new DateTime()

}

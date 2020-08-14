package dictionary.service

import dictionary.Dictionary

trait ServiceInjector {
  def getDictionary(): Dictionary
}

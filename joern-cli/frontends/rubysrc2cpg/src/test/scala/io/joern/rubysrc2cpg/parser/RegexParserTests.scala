package io.joern.rubysrc2cpg.parser

import io.joern.rubysrc2cpg.testfixtures.RubyParserFixture
import org.scalatest.matchers.should.Matchers

class RegexParserTests extends RubyParserFixture with Matchers {
  "Regex" in {
    test("//")
    test("x = //")
    test("puts //")
    test("puts(//)")
    test("puts(1, //)")
    test("""case foo
        | when /^ch_/
        |   bar
        |end""".stripMargin)
    test("""unless /\A([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})\z/i.match?(value)
        |end""".stripMargin)
    test("/(eu|us)/")
    test("x = /(eu|us)/")
    test("puts /(eu|us)/")
    test("puts(/eu|us/)")
    test("%r{a-z}")
    test("%r<eu|us>")
    test("%r[]")
    test("/x#{1}y/")
    test("x = /x#{1}y/")
    test("puts /x#{1}y/")
    test("puts(/x#{1}y/)")
    test("%r{x#{0}|y}")
  }
}
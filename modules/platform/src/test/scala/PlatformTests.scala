package com.indoorvivants.detective

import Platform._

class PlatformTests() extends munit.FunSuite {
  test("OS detection") {
    def props(value: String) = Map("os.name" -> value)

    assertEquals(OS.detect("Mac OS X"), OS.MacOS)
    assertEquals(OS.fromProps(props("Mac OS X")), OS.MacOS)

    assertEquals(OS.detect("Windows 10"), OS.Windows)
    assertEquals(OS.fromProps(props("Windows 10")), OS.Windows)
  }
  test("Bits detection") {
    def props(value: String) = Map("sun.arch.data.model" -> value)

    assertEquals(Bits.detect("64"), Bits.x64)
    assertEquals(Bits.detect("32"), Bits.x32)

    assertEquals(Bits.fromProps(props("64")), Bits.x64)
    assertEquals(Bits.fromProps(props("32")), Bits.x32)
  }

  test("CPU rchitecture detection") {
    def props(value: String) = Map("os.arch" -> value)

    assertEquals(Arch.detect("aarch64"), Arch.Arm)
    assertEquals(Arch.fromProps(props("aarch64")), Arch.Arm)
  }
}

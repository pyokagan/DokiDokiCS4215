package cs4215.events

import org.lwjgl.glfw.GLFW._

sealed trait Key
case object KeySpace extends Key
case object KeyApostrophe extends Key
case object KeyComma extends Key
case object KeyMinus extends Key
case object KeyPeriod extends Key
case object KeySlash extends Key
case object Key0 extends Key
case object Key1 extends Key
case object Key2 extends Key
case object Key3 extends Key
case object Key4 extends Key
case object Key5 extends Key
case object Key6 extends Key
case object Key7 extends Key
case object Key8 extends Key
case object Key9 extends Key
case object KeySemicolon extends Key
case object KeyEqual extends Key
case object KeyA extends Key
case object KeyB extends Key
case object KeyC extends Key
case object KeyD extends Key
case object KeyE extends Key
case object KeyF extends Key
case object KeyG extends Key
case object KeyH extends Key
case object KeyI extends Key
case object KeyJ extends Key
case object KeyK extends Key
case object KeyL extends Key
case object KeyM extends Key
case object KeyN extends Key
case object KeyO extends Key
case object KeyP extends Key
case object KeyQ extends Key
case object KeyR extends Key
case object KeyS extends Key
case object KeyT extends Key
case object KeyU extends Key
case object KeyV extends Key
case object KeyW extends Key
case object KeyX extends Key
case object KeyY extends Key
case object KeyZ extends Key
case object KeyLeftBracket extends Key
case object KeyBackslash extends Key
case object KeyRightBracket extends Key
case object KeyGraveAccent extends Key
case object KeyWorld1 extends Key
case object KeyWorld2 extends Key
case object KeyEscape extends Key
case object KeyEnter extends Key
case object KeyTab extends Key
case object KeyBackspace extends Key
case object KeyInsert extends Key
case object KeyDelete extends Key
case object KeyRight extends Key
case object KeyLeft extends Key
case object KeyDown extends Key
case object KeyUp extends Key
case object KeyPageUp extends Key
case object KeyPageDown extends Key
case object KeyHome extends Key
case object KeyEnd extends Key
case object KeyCapsLock extends Key
case object KeyScrollLock extends Key
case object KeyNumLock extends Key
case object KeyPrintScreen extends Key
case object KeyPause extends Key
case object KeyF1 extends Key
case object KeyF2 extends Key
case object KeyF3 extends Key
case object KeyF4 extends Key
case object KeyF5 extends Key
case object KeyF6 extends Key
case object KeyF7 extends Key
case object KeyF8 extends Key
case object KeyF9 extends Key
case object KeyF10 extends Key
case object KeyF11 extends Key
case object KeyF12 extends Key
case object KeyF13 extends Key
case object KeyF14 extends Key
case object KeyF15 extends Key
case object KeyF16 extends Key
case object KeyF17 extends Key
case object KeyF18 extends Key
case object KeyF19 extends Key
case object KeyF20 extends Key
case object KeyF21 extends Key
case object KeyF22 extends Key
case object KeyF23 extends Key
case object KeyF24 extends Key
case object KeyF25 extends Key
case object KeyKp0 extends Key
case object KeyKp1 extends Key
case object KeyKp2 extends Key
case object KeyKp3 extends Key
case object KeyKp4 extends Key
case object KeyKp5 extends Key
case object KeyKp6 extends Key
case object KeyKp7 extends Key
case object KeyKp8 extends Key
case object KeyKp9 extends Key
case object KeyKpDecimal extends Key
case object KeyKpDivide extends Key
case object KeyKpMultiply extends Key
case object KeyKpSubtract extends Key
case object KeyKpAdd extends Key
case object KeyKpEnter extends Key
case object KeyKpEqual extends Key
case object KeyLeftShift extends Key
case object KeyLeftControl extends Key
case object KeyLeftAlt extends Key
case object KeyLeftSuper extends Key
case object KeyRightShift extends Key
case object KeyRightControl extends Key
case object KeyRightAlt extends Key
case object KeyRightSuper extends Key
case object KeyMenu extends Key

object Key {
  def fromGlfwKey(glfwKey: Int): Key = glfwKey match {
    case GLFW_KEY_SPACE => KeySpace
    case GLFW_KEY_APOSTROPHE => KeyApostrophe
    case GLFW_KEY_COMMA => KeyComma
    case GLFW_KEY_MINUS => KeyMinus
    case GLFW_KEY_PERIOD => KeyPeriod
    case GLFW_KEY_SLASH => KeySlash
    case GLFW_KEY_0 => Key0
    case GLFW_KEY_1 => Key1
    case GLFW_KEY_2 => Key2
    case GLFW_KEY_3 => Key3
    case GLFW_KEY_4 => Key4
    case GLFW_KEY_5 => Key5
    case GLFW_KEY_6 => Key6
    case GLFW_KEY_7 => Key7
    case GLFW_KEY_8 => Key8
    case GLFW_KEY_9 => Key9
    case GLFW_KEY_SEMICOLON => KeySemicolon
    case GLFW_KEY_EQUAL => KeyEqual
    case GLFW_KEY_A => KeyA
    case GLFW_KEY_B => KeyB
    case GLFW_KEY_C => KeyC
    case GLFW_KEY_D => KeyD
    case GLFW_KEY_E => KeyE
    case GLFW_KEY_F => KeyF
    case GLFW_KEY_G => KeyG
    case GLFW_KEY_H => KeyH
    case GLFW_KEY_I => KeyI
    case GLFW_KEY_J => KeyJ
    case GLFW_KEY_K => KeyK
    case GLFW_KEY_L => KeyL
    case GLFW_KEY_M => KeyM
    case GLFW_KEY_N => KeyN
    case GLFW_KEY_O => KeyO
    case GLFW_KEY_P => KeyP
    case GLFW_KEY_Q => KeyQ
    case GLFW_KEY_R => KeyR
    case GLFW_KEY_S => KeyS
    case GLFW_KEY_T => KeyT
    case GLFW_KEY_U => KeyU
    case GLFW_KEY_V => KeyV
    case GLFW_KEY_W => KeyW
    case GLFW_KEY_X => KeyX
    case GLFW_KEY_Y => KeyY
    case GLFW_KEY_Z => KeyZ
    case GLFW_KEY_LEFT_BRACKET => KeyLeftBracket
    case GLFW_KEY_BACKSLASH => KeyBackslash
    case GLFW_KEY_RIGHT_BRACKET => KeyRightBracket
    case GLFW_KEY_GRAVE_ACCENT => KeyGraveAccent
    case GLFW_KEY_WORLD_1 => KeyWorld1
    case GLFW_KEY_WORLD_2 => KeyWorld2
    case GLFW_KEY_ESCAPE => KeyEscape
    case GLFW_KEY_ENTER => KeyEnter
    case GLFW_KEY_TAB => KeyTab
    case GLFW_KEY_BACKSPACE => KeyBackspace
    case GLFW_KEY_INSERT => KeyInsert
    case GLFW_KEY_DELETE => KeyDelete
    case GLFW_KEY_RIGHT => KeyRight
    case GLFW_KEY_LEFT => KeyLeft
    case GLFW_KEY_DOWN => KeyDown
    case GLFW_KEY_UP => KeyUp
    case GLFW_KEY_PAGE_UP => KeyPageUp
    case GLFW_KEY_PAGE_DOWN => KeyPageDown
    case GLFW_KEY_HOME => KeyHome
    case GLFW_KEY_END => KeyEnd
    case GLFW_KEY_CAPS_LOCK => KeyCapsLock
    case GLFW_KEY_SCROLL_LOCK => KeyScrollLock
    case GLFW_KEY_NUM_LOCK => KeyNumLock
    case GLFW_KEY_PRINT_SCREEN => KeyPrintScreen
    case GLFW_KEY_PAUSE => KeyPause
    case GLFW_KEY_F1 => KeyF1
    case GLFW_KEY_F2 => KeyF2
    case GLFW_KEY_F3 => KeyF3
    case GLFW_KEY_F4 => KeyF4
    case GLFW_KEY_F5 => KeyF5
    case GLFW_KEY_F6 => KeyF6
    case GLFW_KEY_F7 => KeyF7
    case GLFW_KEY_F8 => KeyF8
    case GLFW_KEY_F9 => KeyF9
    case GLFW_KEY_F10 => KeyF10
    case GLFW_KEY_F11 => KeyF11
    case GLFW_KEY_F12 => KeyF12
    case GLFW_KEY_F13 => KeyF13
    case GLFW_KEY_F14 => KeyF14
    case GLFW_KEY_F15 => KeyF15
    case GLFW_KEY_F16 => KeyF16
    case GLFW_KEY_F17 => KeyF17
    case GLFW_KEY_F18 => KeyF18
    case GLFW_KEY_F19 => KeyF19
    case GLFW_KEY_F20 => KeyF20
    case GLFW_KEY_F21 => KeyF21
    case GLFW_KEY_F22 => KeyF22
    case GLFW_KEY_F23 => KeyF23
    case GLFW_KEY_F24 => KeyF24
    case GLFW_KEY_F25 => KeyF25
    case GLFW_KEY_KP_0 => KeyKp0
    case GLFW_KEY_KP_1 => KeyKp1
    case GLFW_KEY_KP_2 => KeyKp2
    case GLFW_KEY_KP_3 => KeyKp3
    case GLFW_KEY_KP_4 => KeyKp4
    case GLFW_KEY_KP_5 => KeyKp5
    case GLFW_KEY_KP_6 => KeyKp6
    case GLFW_KEY_KP_7 => KeyKp7
    case GLFW_KEY_KP_8 => KeyKp8
    case GLFW_KEY_KP_9 => KeyKp9
    case GLFW_KEY_KP_DECIMAL => KeyKpDecimal
    case GLFW_KEY_KP_DIVIDE => KeyKpDivide
    case GLFW_KEY_KP_MULTIPLY => KeyKpMultiply
    case GLFW_KEY_KP_SUBTRACT => KeyKpSubtract
    case GLFW_KEY_KP_ADD => KeyKpAdd
    case GLFW_KEY_KP_ENTER => KeyKpEnter
    case GLFW_KEY_KP_EQUAL => KeyKpEqual
    case GLFW_KEY_LEFT_SHIFT => KeyLeftShift
    case GLFW_KEY_LEFT_CONTROL => KeyLeftControl
    case GLFW_KEY_LEFT_ALT => KeyLeftAlt
    case GLFW_KEY_LEFT_SUPER => KeyLeftSuper
    case GLFW_KEY_RIGHT_SHIFT => KeyRightShift
    case GLFW_KEY_RIGHT_CONTROL => KeyRightControl
    case GLFW_KEY_RIGHT_ALT => KeyRightAlt
    case GLFW_KEY_RIGHT_SUPER => KeyRightSuper
    case GLFW_KEY_MENU => KeyMenu
    case _ => throw new AssertionError("unexpected glfw key: " + glfwKey)
  }
}

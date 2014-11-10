package japgolly.scalajs.react.vdom

import scala.scalajs.js
import japgolly.scalajs.react.React

private[vdom] object VDomBuilder  {

  def specialCaseAttrs =
    Set("onBlur", "onChange", "onClick", "onFocus", "onKeyDown", "onKeyPress", "onKeyUp", "onLoad", "onMouseDown"
      , "onMouseMove", "onMouseOut", "onMouseOver", "onMouseUp", "onSelect", "onScroll", "onSubmit", "onReset"
      , "readOnly")

  def specialNameAttrs =
    Map("class" -> "className", "for" -> "htmlFor")

  val attrTranslations =
    specialCaseAttrs.toList.map(x => x.toLowerCase -> x).toMap ++ specialNameAttrs
}

private[vdom] final class VDomBuilder {
  import VDomBuilder._

  private[this] var props = new js.Object
  private[this] var style = new js.Object
  private[this] var vdomArgs = js.Array[ReactFragT](props)

  def appendChild(c: ReactFragT): Unit =
    vdomArgs.push(c)

  def addAttr(k: String, v: js.Any): Unit =
    set(props, attrTranslations.getOrElse(k, k), v)

  def addStyle(k: String, v: String): Unit =
    set(style, k, v)

  @inline private[this] def set(o: js.Object, k: String, v: js.Any): Unit =
    o.asInstanceOf[js.Dynamic].updateDynamic(k)(v)

  @inline private[this] def hasStyle = js.Object.keys(style).length != 0


  def render(tag: String) = {
    if (hasStyle) set(props, "style", style)

    // Following reacts convention of built-in DOM is lower-case
    // components are upper case (title case I believe)
    // if (tag.head.isLower)
    React.DOM.applyDynamic(tag)(vdomArgs: _*).asInstanceOf[ReactOutput]
    // not working
    // else 
    // React.createElement(tag, null, vdomArgs: _*).asInstanceOf[ReactOutput]
  }
}

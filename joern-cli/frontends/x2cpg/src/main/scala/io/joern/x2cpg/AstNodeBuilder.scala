package io.joern.x2cpg

import io.shiftleft.codepropertygraph.generated.nodes.{
  NewAnnotation,
  NewBlock,
  NewCall,
  NewControlStructure,
  NewFieldIdentifier,
  NewIdentifier,
  NewLiteral,
  NewLocal,
  NewMember,
  NewMethod,
  NewMethodParameterIn,
  NewMethodRef,
  NewReturn,
  NewTypeDecl,
  NewTypeRef,
  NewUnknown
}
import org.apache.commons.lang.StringUtils

trait AstNodeBuilder[Node, NodeProcessor] { this: NodeProcessor =>
  protected def line(node: Node): Option[Integer]
  protected def column(node: Node): Option[Integer]
  protected def lineEnd(node: Node): Option[Integer]
  protected def columnEnd(element: Node): Option[Integer]

  protected def unknownNode(node: Node, code: String): NewUnknown = {
    NewUnknown()
      .parserTypeName(node.getClass.getSimpleName)
      .code(code)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def annotationNode(node: Node, code: String, name: String, fullName: String): NewAnnotation = {
    NewAnnotation()
      .code(code)
      .name(name)
      .fullName(fullName)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def methodRefNode(node: Node, code: String, methodFullName: String, typeFullName: String): NewMethodRef = {
    NewMethodRef()
      .code(code)
      .methodFullName(methodFullName)
      .typeFullName(typeFullName)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def memberNode(node: Node, name: String, code: String, typeFullName: String): NewMember =
    memberNode(node, name, code, typeFullName, Seq())

  protected def memberNode(
    node: Node,
    name: String,
    code: String,
    typeFullName: String,
    dynamicTypeHints: Seq[String] = Seq()
  ): NewMember = {
    NewMember()
      .code(code)
      .name(name)
      .typeFullName(typeFullName)
      .dynamicTypeHintFullName(dynamicTypeHints)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def literalNode(
    node: Node,
    code: String,
    typeFullName: String,
    dynamicTypeHints: Seq[String] = Seq()
  ): NewLiteral = {
    NewLiteral()
      .code(code)
      .typeFullName(typeFullName)
      .dynamicTypeHintFullName(dynamicTypeHints)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def typeRefNode(node: Node, code: String, typeFullName: String): NewTypeRef = {
    NewTypeRef()
      .code(code)
      .typeFullName(typeFullName)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  def typeDeclNode(
    node: Node,
    name: String,
    fullName: String,
    fileName: String,
    inheritsFrom: Seq[String],
    alias: Option[String]
  ): NewTypeDecl =
    typeDeclNode(node, name, fullName, fileName, name, "", "", inheritsFrom, alias)

  protected def typeDeclNode(
    node: Node,
    name: String,
    fullName: String,
    filename: String,
    code: String,
    astParentType: String = "",
    astParentFullName: String = "",
    inherits: Seq[String] = Seq.empty,
    alias: Option[String] = None
  ): NewTypeDecl = {
    NewTypeDecl()
      .name(name)
      .fullName(fullName)
      .code(code)
      .isExternal(false)
      .filename(filename)
      .astParentType(astParentType)
      .astParentFullName(astParentFullName)
      .inheritsFromTypeFullName(inherits)
      .aliasTypeFullName(alias)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def parameterInNode(
    node: Node,
    name: String,
    code: String,
    index: Int,
    isVariadic: Boolean,
    evaluationStrategy: String,
    typeFullName: String
  ): NewMethodParameterIn =
    parameterInNode(node, name, code, index, isVariadic, evaluationStrategy, Some(typeFullName))

  protected def parameterInNode(
    node: Node,
    name: String,
    code: String,
    index: Int,
    isVariadic: Boolean,
    evaluationStrategy: String,
    typeFullName: Option[String] = None
  ): NewMethodParameterIn = {
    NewMethodParameterIn()
      .name(name)
      .code(code)
      .index(index)
      .order(index)
      .isVariadic(isVariadic)
      .evaluationStrategy(evaluationStrategy)
      .lineNumber(line(node))
      .columnNumber(column(node))
      .typeFullName(typeFullName.getOrElse("ANY"))
  }

  def callNode(node: Node, code: String, name: String, methodFullName: String, dispatchType: String): NewCall =
    callNode(node, code, name, methodFullName, dispatchType, None, None)

  def callNode(
    node: Node,
    code: String,
    name: String,
    methodFullName: String,
    dispatchType: String,
    signature: Option[String],
    typeFullName: Option[String]
  ): NewCall = {
    val out =
      NewCall()
        .code(code)
        .name(name)
        .methodFullName(methodFullName)
        .dispatchType(dispatchType)
        .lineNumber(line(node))
        .columnNumber(column(node))
    signature.foreach { s => out.signature(s) }
    typeFullName.foreach { t => out.typeFullName(t) }
    out
  }

  protected def returnNode(node: Node, code: String): NewReturn = {
    NewReturn()
      .code(code)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def controlStructureNode(node: Node, controlStructureType: String, code: String): NewControlStructure = {
    NewControlStructure()
      .parserTypeName(node.getClass.getSimpleName)
      .controlStructureType(controlStructureType)
      .code(code)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def blockNode(node: Node, code: String, typeFullName: String): NewBlock = {
    NewBlock()
      .code(code)
      .typeFullName(typeFullName)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def fieldIdentifierNode(node: Node, name: String, code: String): NewFieldIdentifier = {
    NewFieldIdentifier()
      .canonicalName(name)
      .code(code)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  protected def localNode(
    node: Node,
    name: String,
    code: String,
    typeFullName: String,
    closureBindingId: Option[String] = None
  ): NewLocal =
    NewLocal()
      .name(name)
      .code(code)
      .typeFullName(typeFullName)
      .closureBindingId(closureBindingId)
      .lineNumber(line(node))
      .columnNumber(column(node))

  protected def identifierNode(
    node: Node,
    name: String,
    code: String,
    typeFullName: String,
    dynamicTypeHints: Seq[String] = Seq()
  ): NewIdentifier = {
    NewIdentifier()
      .name(name)
      .typeFullName(typeFullName)
      .code(code)
      .dynamicTypeHintFullName(dynamicTypeHints)
      .lineNumber(line(node))
      .columnNumber(column(node))
  }

  def methodNode(node: Node, name: String, fullName: String, signature: String, fileName: String): NewMethod = {
    methodNode(node, name, name, fullName, Some(signature), fileName)
  }

  protected def methodNode(
    node: Node,
    name: String,
    code: String,
    fullName: String,
    signature: Option[String],
    fileName: String,
    astParentType: Option[String] = None,
    astParentFullName: Option[String] = None
  ): NewMethod = {
    val node_ =
      NewMethod()
        .name(StringUtils.normalizeSpace(name))
        .code(code)
        .fullName(StringUtils.normalizeSpace(fullName))
        .filename(fileName)
        .astParentType(astParentType.getOrElse("<empty>"))
        .astParentFullName(astParentFullName.getOrElse("<empty>"))
        .isExternal(false)
        .lineNumber(line(node))
        .columnNumber(column(node))
        .lineNumberEnd(lineEnd(node))
        .columnNumberEnd(columnEnd(node))
    signature.foreach { s => node_.signature(StringUtils.normalizeSpace(s)) }
    node_
  }
}
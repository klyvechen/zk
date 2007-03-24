<%--
window.dsp

{{IS_NOTE
	Purpose:
		
	Description:
		z.autoz:
			Automatically adjust z-index onmousedown (au.js)
	History:
		Tue May 31 19:37:23     2005, Created by tomyeh
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%><%@ taglib uri="/WEB-INF/tld/web/core.dsp.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/zk/core.dsp.tld" prefix="z" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<c:set var="titlesc" value="${self.titleSclass}"/>
<div id="${self.uuid}" z.type="zul.wnd.Wnd" z.autoz="true"${self.outerAttrs}${self.innerAttrs}>
<c:choose>
 <c:when test="${empty self.caption and empty self.title}">
  <c:if test="${c:isExplorer() and !c:isExplorer7()}"><%-- Bug 1579515: to clickable, a child with 100% width is required for DIV --%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr height="1px"><td></td></tr>
</table>
  </c:if>
 </c:when>
 <c:otherwise>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <c:choose>
   <c:when test="${empty self.caption}">
<tr id="${self.uuid}!caption" class="title">
<td class="l${titlesc}"></td>
<td class="m${titlesc}"><c:out value="${self.title}"/></td>
<c:if test="${self.closable}">
	<td width="16" class="m${titlesc}"><img id="${self.uuid}!close" src="${c:encodeURL('~./zul/img/close-off.gif')}"/></td>
</c:if>
<td class="r${titlesc}"></td>
</tr>
   </c:when>
   <c:otherwise>
<tr id="${self.uuid}!caption"><%-- title and closable button are generated by caption.dsp --%>
<td class="l${titlesc}"></td>
<td class="m${titlesc}">${z:redraw(self.caption, null)}</td>
<td class="r${titlesc}"></td>
</tr>
   </c:otherwise>
  </c:choose>
</table>
  <c:set var="wcExtStyle" value="border-top:0;"/><%-- used below --%>
 </c:otherwise>
</c:choose>
<c:set var="wcExtStyle" value="${c:cat(wcExtStyle, self.contentStyle)}"/>
<div id="${self.uuid}!cave" class="${self.contentSclass}"${c:attr('style',wcExtStyle)}>
<c:forEach var="child" items="${self.children}">
<c:if test="${self.caption != child}">${z:redraw(child, null)}</c:if>
</c:forEach>
</div><%-- we don't generate shadow here since it looks odd when on top of modal mask --%>
</div>

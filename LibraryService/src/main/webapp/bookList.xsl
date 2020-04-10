<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
  <h2>All Books</h2>
  <table border="1">
    <tr bgcolor="#3aef27">
        <th>id</th>
        <th>title</th>
	<th>isbn</th>
	<th>description</th>
	<th>author</th>
	<th>publisher</th>
    </tr>
    <xsl:for-each select="bookList/book">
    <tr>
      	<td><xsl:value-of select="id"/></td>
	<td><xsl:value-of select="title"/></td>
      	<td><xsl:value-of select="isbn"/></td>
	<td><xsl:value-of select="description"/></td>
	<td><xsl:value-of select="author"/></td>
	<td><xsl:value-of select="publisher"/></td>
    </tr>
    </xsl:for-each>
  </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${model.title}</title>
<style>
@page { size: A4 landscape;}
body{
	font-family: SimSun;
}
table{
	width:1000px;
	table-layout:fixed;
	word-break:break-strict;
}
</style>
</head>
<body>
<table width="100%" border="0" style="table-layout:fixed;word-break:break-strict;">
  <tr>
    <td>
    ${model.title1}
    </td>
  </tr>
  <tr>
    <td>
	${model.content}
    </td>
  </tr>
</table>
<div id="content">

</div>
</body>
</html>

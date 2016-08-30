(function () {
	String.prototype.trim=function () {
		return this.replace(/^\s+/,'').replace(/\s+$/,'');
	};
	function SortableTable(table) {
		this.table=table;
		
		var div=document.createElement("div");
		div.innerHTML='<img class="sortDirImage" src="desc.gif" />';
		this.table.sortDirImage=div.firstChild;
		
		//1、得到头
		this.headers=getElementsByClassName("sortableCol",table.tHead.rows[0]);
		//2、得到体
		this.tBody=table.tBodies[0];
		//3、表体中的所有行
		this.rows=this.tBody.rows;
		var i=0,vt,_this=this;
		//4、对表头所有的列进行处理
		for (;i<this.headers.length;i++) {
//			alert($(this.headers[i]).attr("notSort")+"不排序的纸质")
			if($(this.headers[i]).attr("notSort") == "notSort"){
				//不排序则不处理
			}else{
				//得到该列的值类型
				vt=this.headers[i].getAttribute("valuetype") || "string";
				//这里是对值类型和日期格式化形参放在同一个属性中进行表示
				vt=vt.split(/\s+/);
				//为每一行的值类型不同，而注册相应的函数
				this.headers[i].compare=SortableTable.compareMethods[vt[0]];
				this.headers[i].compareParam=vt[1];//拿到就是值的具体格式化文本
				//为头行中的每一列单元格注册鼠标点击事件
				addEvent(this.headers[i],"click",function (evt) {
					evt.preventDefault();//通知浏览器不要执行与事件关联的默认动作。
					//开始进行排序.排序只要知道是具体那一列就可以
					_this.sortCol(this.cellIndex,-this.currentDir || 1);
				});
			}
		}
	}
	extend(SortableTable,{
		compareMethods:{
			"string":function (a,b,param,dir) {
				return a.localeCompare(b)*dir;
			},
			"number":function (a,b,param,dir) {
				a=parseFloat(a);
				b=parseFloat(b);
				if (a>b) return dir;
				if (a<b) return -dir;
				return 0;
			},
			"date":function (a,b,param,dir) {
				a=dateDecode(a,param).getTime();
				b=dateDecode(b,param).getTime();
				if (a>b) return dir;
				if (a<b) return -dir;
				return 0;
			}
		}
	});
	SortableTable.prototype={
		//如何来显示排序的算法呢？
		sortCol:function (colIndex,dir) {
			//1、根据该列值来获取该列所对应的所有数据,用来排序（排序的第一个步骤就是要获取到排序的数据）
			var ary=this.collect(colIndex);
			var _this=this;//表格对象
			dir = dir || 1;
			this.headers[colIndex].currentDir=dir;//保存当前的排序状态
			//对该数组进行排序
			ary.sort(function (a,b) {
				return _this.headers[colIndex].compare(a.value,b.value,_this.headers[colIndex].compareParam,dir);
			});
			var frag=document.createDocumentFragment();//创建文档碎片
			for (var i=0;i<ary.length;i++) {
				frag.appendChild(ary[i].node);
			}
			this.tBody.appendChild(frag);
			//sortDirImage
			this.table.sortDirImage.src=(dir>0?"asc":"desc")+".gif";
			this.headers[colIndex].appendChild(this.table.sortDirImage);
			addClass(this.headers[colIndex],"sorting");
			for (i=0;i<this.headers.length;i++) {
				delClass(this.headers[i],"sorting");
			}
		},
			//根据列值获取该列的所有数据
		collect:function (colIndex) {
			var i=0,a=[];
			//表体中的所有行
			for (;i<this.rows.length;i++) {
				a.push({
					node:this.rows[i],//获取具体行，返回的是一个具体对象
					value:getInnerText(this.rows[i].cells[colIndex])//根据具体的对象来获取器列中的文本值
				});
			}
			//将所需要排序的数据进行返回
			return a;
		}
	};

//在这里为表单添加事件
	addEvent(window,"load",function () {
		var tables=getElementsByClassName("sortableTable");
		for (var i=0;i<tables.length;i++)
			new SortableTable(tables[i]);
	});


	function getInnerText(node) {
		if (node.nodeType==3)
			return node.nodeValue.trim();
		else if (node.nodeType==1) {
			for (var i=0,t="";i<node.childNodes.length;i++) {
				t+=getInnerText(node.childNodes[i]);
			}
			return t;
		} else {
			return "";
		}
	}


	function getElementsByClassName(name,context) {
		context=context || document;
		if (context.getElementsByClassName) {
			return context.getElementsByClassName(name);
		} else {
			var nodes=context.getElementsByTagName("*"),ret=[];
			for (var i=0;i<nodes.length;i++) {
				if (hasClass(nodes[i],name)) ret.push(nodes[i]);
			}
			return ret;
		}
	}
	function hasClass(node,name) {
		var names=node.className.split(/\s+/);
		for (var i=0;i<names.length;i++)  {
			if (names[i]==name) return true;
		}
		return false;
	}
	
	function addClass(node,name) {
		if (!hasClass(node,name)) node.className += " "+name;
	}
	
	function delClass(node,name) {
		var names=node.className.split(/\s+/);
		for (var i=0;i<names.length;i++ ){
			if (names[i]==name) delete names[i];
		}
		node.className=names.join(" ");
	} 
	

	
	function addEvent(o,evtype,fn) {
		if (o.addEventListener) {
			o.addEventListener(evtype,fn,false);
		} else if (o.attachEvent) {
			o.attachEvent("on"+evtype,function () {
				fn.call(o,fixEvent(window.event));
			});
		} else {
			throw new Error("No event bind method can be used!!!");
		}
	}
	function fixEvent(evt) {
		evt.layerX=evt.offsetX;
		evt.layerY=evt.offsetY;
		evt.target=evt.srcElement;
		if (evt.type=="mouseout")
			evt.relatedTarget=evt.toElement;
		else if (evt.type=="mouseover")
			evt.relatedTarget=evt.fromElement;
		else 
			evt.relatedTarget=evt.target;
		
		evt.stopPropagation=function () {
			this.cancelBubble=true;
		};
		evt.preventDefault=function () {
			this.returnValue=false;
		};
		return evt;
	}
	
	function dateDecode(s,format) {
		var a=s.match(/\d+/g),d=new Date();
		format=format.split("");
		for (var i=0;i<format.length;i++) {
			switch(format[i]) {
				case "Y":d.setFullYear(a[i]);break;
				case "m":d.setMonth(a[i]-1);break;
				case "d":d.setDate(a[i]);break;
				case "H":d.setHours(a[i]);break;
				case "i":d.setMinutes(a[i]);break;
				case "s":d.setSeconds(a[i]);break;
			}
		}
		return d;
	}	
	function extend(dest,src) {
		for (var i in src) {
			dest[i]=src[i];
		}
	}	
})();
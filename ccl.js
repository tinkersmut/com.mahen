/**
 * 
 * @param name
 * @param desc
 * @returns {Category}
 */
function Category(name, desc){
	this.name = name;
	this.description = desc;
	this.subcats = new Array();
}

/**
 * 
 * @param category
 * @param name
 * @param desc
 * @returns {SubCategory}
 */
function SubCategory(category, name, desc){
	this.name = name;
	this.parent = category;
	this.description = desc;
	this.eccns = new Array();
}

/**
 * 
 * @param subcat
 * @param name
 * @param desc
 * @returns {ECCN}
 */
function ECCN(subcat, name, desc){
	this.parent = subcat;
	this.name = name;
	this.description = desc;
	this.items = new Array();
}

/**
 * 
 * @param eccn
 * @param name
 * @param desc
 * @returns {Item}
 */
function Item(eccn, name, desc){
	this.parent = eccn;
	this.name = name;
	this.description = desc;
}

/**
 * 
 */
function CCLParser(){
	this.html = undefined;
	this.lines = undefined;
	this.categories = undefined;
	this.subcats = undefined;
	
	/**
	 * 
	 */
	this.constructor = function(){
		var url = 'http://ecfr.gpoaccess.gov/cgi/t/text/text-idx?c=ecfr&sid=c5cc9a1c749a6f225283bdfa124431d0&rgn=div9&view=text&node=15:2.1.3.4.45.0.1.3.87&idno=15';
		var h = new http(url);
		h.username='';
		h.password='';
		html = h.get.submit().data.getString();
		lines = html.split(/[\n]/);
		// TODO: find "current as of <date>" and parse out date

		var i;
		for(i in lines){
			if(lines[i].match(/Category \d+/g)){
				break;
			}
		}
		
		// should be 1 line with all cats etc
		if(i >= lines.length){
			logger.error('Failed to find definition line');
			return;
		}

		this.html = lines[i];
		this.lines = html.split(/[<]p[>]/);
		this.categories = new Array();
		this.subcats = new Array();
	}
	
	/**
	 * 
	 */
	this.parse = function(){

		for(i in this.lines){
			
			// category declaration line
			if(this.isCategoryLine(this.lines[i])){
				lastCati = i;
				lastSubi = undefined;
				lastClassi = undefined;
				continue;
			}
			
			// look for sub cat dec
			if(lastCati && !lastSubi && !lastClassi){
				if(this.isSubLine(this.lines[i])){
					lastSubi = i;
				}
			}
			
			// look for class line
			else if(lastCati && lastSubi && !lastClassi){
				
			}
			
			// look for items
			else if(lastCati && lastSubi && lastClassi){
				
			}
		}
	}
	
	/**
	 * 
	 */
	this.isSubLine = function(line){
		var match = line.match(/^[A-Z][.]/);
		if(!match){
			return false;
		}
		var category = ''+(this.categories.length - 1)+match[0].replace(/[.]/g,'');
		this.subcats[category] = line.replace(/^[A-Z][.]/g,'').replace(/[<][/]p[>]/g,'').replace(/[&]rdquo[;]/g,'"').replace(/[&]ldquo[;]/g,'"');
		return true;
	}
	
	/**
	 * 
	 */
	this.isCategoryLine = function(line){
		var match = line.match(/^Category \d+/);
		
		// category
		if(!match){
			return false;
		}
		match = match[0].match(/\d+/);
		var catnum = new Number(match[0]);
		var description = line.replace(/Category \d+([&]mdash[;])?/,'').replace(/[<][/]p[>]/g,'').replace(/[&]rdquo[;]/g,'"').replace(/[&]ldquo[;]/g,'"');
		if(description){
			if(!this.categories[catnum]){
				this.categories[catnum] = description;
				return true;
			}else{
			  logger.debug('Multiple category definitions '+line);
			}
		}
		return false;
	}
	
	/**
	 * 
	 */
	this.dump = function(){
		var i;
		for(i in this.categories){
			  logger.info(''+i+':'+this.categories[i]);
		}		
		for(i in this.subcats){
			logger.info(i+':'+this.subcats[i]);
		}
	}
	
	this.dummy = this.constructor();
}


var parser = new CCLParser();
parser.parse();
parser.dump();
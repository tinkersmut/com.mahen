
if(id3.artist && id3.title){
  if(id3.title.indexOf(id3.artist) > -1){
    var r = id3.artist.replace("\\W","\\\\W");
    id3.title = id3.title.replace(r,"");
  }
  var x = id3.artist + " - " + id3.title;
  
  if(x.indexOf("_") > -1){
    x = x.replace("\\_"," ");
  }

  println('Renaming '+id3.filename+' to '+x);
  id3.filename=x;
}

if(!id3.artist){
  var a = id3.filename.split("\\-");
  if(a && a.length > 1){
    id3.artist = a[0];
    println('artist = '+id3.artist);
  }
}

if(!id3.title){
  var a = id3.filename.split("\\-");
  if(a && a.length > 1){
    id3.title = a[1];
    println('title = '+id3.title);
  }  
}

id3.save();
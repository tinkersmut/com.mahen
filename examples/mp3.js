var errors = new Array();
var underscore = new Array();
var empty = new Array();
var processfiles = new Array();
var totalprocessed = 0;
var totaldirs = 0;

function scan(files){
  for(var i=0;i<files.length;i++) {
    if(files[i].isdir && (files[i].path != '.' || files[i].path != '..')){
      scan(files[i].files);
      totaldirs++;
      continue;
    }else if(files[i].ext == "mp3"){
      processfiles.push(files[i]);
    }
    totalprocessed++;
    if(totalprocessed%100 == 0)
      out.print('.');
  }
}

function validate(files){
  for(var i=0;i<files.length;i++) {
    try{
      var mp3 = new MP3(files[i].path);
      if(files[i].path.indexOf('_') > -1 || mp3.artist.indexOf('_') > -1 || mp3.title.indexOf('_') > -1){
        underscore.push(mp3);
      }
      else if (mp3.artist == '' || mp3.title == ''){
        empty.push(mp3);
      }
      
    }catch(e){
      errors.push(files[i].path);
    }
    if(i%100 == 0 && i != 0)
      out.print('.');
  }
}

var dir = new Directory("x:/upload");
scan(dir.files);
out.println('');
out.println('Processing '+processfiles.length);
validate(processfiles);
out.println('Processed '+totalprocessed+' files.');
out.println('Processed '+totaldirs+' dirs.');
out.println('there are '+underscore.length+' _ files.');
out.println('there are '+empty.length+' empty files.');
out.println('there are '+errors.length+' bad files.');

console.log("add_contact");

document.querySelector("#image_file_input")
.addEventListner('change',function(event) {
    let file = event.target.file[0]
    let reader= new FileReader()
    reader.onload = function(){
        document
              .querySelector("#upload_image_preview")
              .setAttribute("src",reader.result)
        //document.getElementById("upload_image_preview").src=reader.result
    }
    reader.readAsDataURL(file)
})
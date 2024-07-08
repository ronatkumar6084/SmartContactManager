console.log("this is script file");
//alert("Scrip loaded....")

let currentTheme = getTheme();

changeTheme(currentTheme)

function changeTheme(){
    document.querySelector('html').classList.add(currentTheme)
    const changeThemeButton = document.querySelector('#theme_change_button');
    //changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "dark" : "light" ;
    changeThemeButton.addEventListener("click", (event) =>{
      const oldTheme = currentTheme
        if(currentTheme = "dark"){
            currentTheme="light";
        }else{
            currentTheme="dark";
        }
        setTheme(currentTheme);
        document.querySelector('html').classList.remove(oldTheme)
        document.querySelector('html').classList.add(currentTheme)
        //changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "dark" : "light" ;
    });
    
}

function setTheme(theme){
    localStorage.setItem("theme",theme);
}

function getTheme(){
    let theme = localStorage.getItem("theme")
    return theme ? theme : "light";
}
   
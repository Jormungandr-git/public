$(document).ready(function(){

    var navBar = false;
    var menu=  document.getElementById("nav_menu_logo");
    document.getElementById("nav_menu_logo").addEventListener("mouseover", function(){
        if(!navBar) {menu.setAttribute("src","../common/menu_icon_active.png");}
    });
    document.getElementById("nav_menu_logo").addEventListener("mouseout", function(){
        if(!navBar){menu.setAttribute("src","../common/menu_icon.png");}
    });
    document.getElementById("nav_menu_logo").addEventListener("click", function(){
        console.log("on click event");
       
       if (navBar) {
        document.getElementById("nav_menu_logo").setAttribute("src","../common/menu_icon.png");
            document.getElementById("nav_menu").style.visibility = "hidden";
            document.getElementById("navigation_bar").style.visibility = "visible";
            document.getElementById("container").style.visibility = "visible";
            navBar = false;

       }else{
        document.getElementById("nav_menu_logo").setAttribute("src","../common/menu_icon_back.png");
        document.getElementById("nav_menu").style.visibility = "visible";
            document.getElementById("navigation_bar").style.visibility = "hidden";
            document.getElementById("container").style.visibility = "hidden";
            navBar = true;
       }
       
    });




});

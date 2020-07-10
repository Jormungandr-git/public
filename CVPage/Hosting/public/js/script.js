
$(document).ready(function(){

    ///console.log("Script started!");
    


    ////////    MEDIA BUTTONS CLICK [IMPROVE IN FUTURE] ////////

    let element = document.getElementById("fb");
    element.addEventListener("mouseover",function(){
        element.setAttribute("src","./common/fActive.png");  
    })
    element.addEventListener("mouseout",function(){
        element.setAttribute("src","./common/f.png");   
    })
    
    let element2 = document.getElementById("in");
    element2.addEventListener("mouseover",function(){
        element2.setAttribute("src","./common/inActive.png");  
    })
    element2.addEventListener("mouseout",function(){
        element2.setAttribute("src","./common/in.png");   
    })

    let element3 = document.getElementById("insta"); 
    element3.addEventListener("mouseover",function(){
        element3.setAttribute("src","./common/instaActive.png");  
    })
    element3.addEventListener("mouseout",function(){
        element3.setAttribute("src","./common/insta.png");   
    })

    let element4 = document.getElementById("g+");
    element4.addEventListener("mouseover",function(){
        element4.setAttribute("src","./common/g+Active.png");  
    })
    element4.addEventListener("mouseout",function(){
        element4.setAttribute("src","./common/g+.png");   
    })

    //////////////////////////////////////////////////////////////////


    document.getElementById("infoImg").addEventListener("click",function(){

        if(document.getElementById("infobubble").style.display == "none"){
            document.getElementById("infobubble").style.display = "block";
        }else{
            document.getElementById("infobubble").style.display = "none";
        }
        
        console.log("HOVER");
    });
})

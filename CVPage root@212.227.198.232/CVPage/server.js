const express = require('express');
const app = express();

/*
app.get('/', (req, res) => {
    res.sendFile('/home/syllvannass/Desktop/CV Page/index.html');
});
*/
app.use(express.static(__dirname +'/public'))

app.listen(1555,function(){
    console.log("Listening on Port 1555!");
})
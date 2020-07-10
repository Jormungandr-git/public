var express = require('express');
var router = express.Router();
var path = require("path");
require(path.join(__dirname, "../public/javascripts/Prototypes.js"));
const { exec } = require('child_process');
const defCommand = "../bitcoin/src/bitcoin-cli -conf=../bitcoin/regtest.conf";


/* GET home page. */
router.post('/', function(req, res, next) { 
  const filePath = path.join(__dirname, "../../public/html/map.html");
  console.log(req.body);
  res.json(req.body);
});
	
router.get("/", function(req,res,next){
  res.redirect("/register")
})

router.get('/map', function(req, res, next) { 
  res.sendFile(path.join(__dirname, "../public/html/map.html"));
});

router.get('/border', function(req, res, next) { 
  res.sendFile(path.join(__dirname, "../public/html/border.html"));
});

router.get("/register", function(req, res, next){
  res.sendFile(path.join(__dirname, "../public/html/create.html"));
})
router.post('/create', async function(req, res, next) {
  console.log(req.body);
  const { countryTo, firstname, lastname, ID } = req.body;

  const userHex = await JSON.stringify({
    "name": firstname,
    "lastName": lastname,
    "id": ID,
    "nationality": countryTo,
    "status": "Warned"
  }).toHex();

  let input = await JSON.stringify({
    "to": countryTo,
    "from": null,
    "previousHash": null,
    "user": userHex
  });

  console.log(`New user: ${input}`);

  input = input.toHex();

  const crt = await runExec(`${defCommand} createrawtransaction "[]" "{\\"data\\":\\"${input}\\"}"`);
  const frt = await runExec(`${defCommand} fundrawtransaction ${crt}`);
  const srt = await runExec(`${defCommand} signrawtransaction ${await JSON.parse(frt.cleanData()).hex}`);
  const transactionID = await runExec(`${defCommand} sendrawtransaction ${await JSON.parse(srt.cleanData()).hex}`);
  ` New user: ${transactionID}`.sendLog();
  res.json({
    "newHash":transactionID
  });
});

router.post('/travel', async function(req, res, next) {
  const { countryFrom, countryTo, currentHash } = req.body;

  const grt = await runExec(`${defCommand} gettransaction ${currentHash}`);
  const drt = await runExec(`${defCommand} decoderawtransaction ${await JSON.parse(grt.cleanData()).hex}`);
  let user;
  for(i of await JSON.parse(drt.cleanData()).vout) {
      if(i.value == 0.00) {
          const previusData = await JSON.parse(await i.scriptPubKey.hex.slice(6).fromHex().cleanData());
          console.log("Previus data: " + previusData.user);
          user = previusData.user;
          if(previusData.to != countryFrom) return console.log(`ERROR: to and from dont match (${previusData.to}, ${countryFrom})`);
          else break;
      }
  }

  let hash = currentHash;

  let input = await JSON.stringify({
    "to": countryTo,
    "from": countryFrom,
    "previousHash": hash,
    "user": user
  });

  console.log(input);

  input = input.toHex();

  const crt = await runExec(`${defCommand} createrawtransaction "[]" "{\\"data\\":\\"${input}\\"}"`);
  const frt = await runExec(`${defCommand} fundrawtransaction ${crt}`);
  const srt = await runExec(`${defCommand} signrawtransaction ${await JSON.parse(frt.cleanData()).hex}`);
  const transactionID = await runExec(`${defCommand} sendrawtransaction ${await JSON.parse(srt.cleanData()).hex}`);
  ` Travel: ${transactionID}`.sendLog();
  res.json({
    "newHash": transactionID
  });
});

router.post('/path', async function(req, res, next) {
  console.log(req.body);
  const { currentHash } = req.body;
  const paths = await readPath(currentHash, []);
  console.log(paths);
  await res.json(await paths.reverse());
  async function readPath(currentHash, array) {
    const grt = await runExec(`${defCommand} gettransaction ${currentHash}`);
    const drt = await runExec(`${defCommand} decoderawtransaction ${await JSON.parse(grt.cleanData()).hex}`);
    for(i of await JSON.parse(drt).vout) {
        if(i.value == 0.00) {
            const previusData = await JSON.parse(await i.scriptPubKey.hex.slice(6).fromHex().cleanData());
            array.push([previusData.from, previusData.to]);
            if(previusData.previousHash == null) return array;
            else return readPath(previusData.previousHash, array);
        }
    }
  }
});

router.post('/getUser', async function(req, res, next) {
  console.log(req.body);
  const { currentHash } = req.body;
  const grt = await runExec(`${defCommand} gettransaction ${currentHash}`);
  const drt = await runExec(`${defCommand} decoderawtransaction ${await JSON.parse(grt.cleanData()).hex}`);
  for(i of await JSON.parse(drt).vout) {
    if(i.value == 0.00) {
      const previusData = await JSON.parse(await i.scriptPubKey.hex.slice(6).fromHex().cleanData());
      console.log(previusData.user);
      console.log(previusData.user.fromHex());
      return res.json(previusData.user.fromHex());
    }
  }
});

function runExec(command) {
    return new Promise(function (resolve, reject) {
        exec(command, (error, stdout, stderr) => {
            if(!error) resolve(stdout.replace("\n", ""));
            else reject(error);
        });
    });
}

module.exports = router;

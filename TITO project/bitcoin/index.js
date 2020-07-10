require("./Prototypes.js");

const { exec } = require('child_process');

console.log("Running!");
 runServer();
async function runServer() {
    const defCommand = "./src/bitcoin-cli -conf=/home/syllvannass/Desktop/bitcoin-sv-0.2.2.beta/regtest.conf";

    async function setUpUser(countryTo) {
        const input = await JSON.stringify({
            "to": countryTo,
            "from": null,
            "previousHash": null
        }).toHex();
    
        const crt = await runExec(`${defCommand} createrawtransaction "[]" "{\\"data\\":\\"${input}\\"}"`);
        const frt = await runExec(`${defCommand} fundrawtransaction ${crt}`);
        const srt = await runExec(`${defCommand} signrawtransaction ${await JSON.parse(frt.cleanData()).hex}`);
        const transactionID = await runExec(`${defCommand} sendrawtransaction ${await JSON.parse(srt.cleanData()).hex}`);
        //console.log("TransactionID: " + transactionID);
        return transactionID;
    }

    async function travel(countryFrom, countryTo, currentHash) {
        const input = await JSON.stringify({
            "to": countryTo,
            "from": countryFrom,
            "previousHash": currentHash
        }).toHex();
    
        const grt = await runExec(`${defCommand} gettransaction ${currentHash}`);
        const drt = await runExec(`${defCommand} decoderawtransaction ${await JSON.parse(grt.cleanData()).hex}`);
        for(i of await JSON.parse(drt.cleanData()).vout) {
            if(i.value == 0.00) {
                const previusData = await JSON.parse(await i.scriptPubKey.hex.slice(6).fromHex().cleanData());
                if(previusData.to != countryFrom) return console.log("ERROR: to and from dont match");
                else break;
            }
        }

        const crt = await runExec(`${defCommand} createrawtransaction "[]" "{\\"data\\":\\"${input}\\"}"`);
        const frt = await runExec(`${defCommand} fundrawtransaction ${crt}`);
        const srt = await runExec(`${defCommand} signrawtransaction ${await JSON.parse(frt.cleanData()).hex}`);
        const transactionID = await runExec(`${defCommand} sendrawtransaction ${await JSON.parse(srt.cleanData()).hex}`);
        //console.log("TransactionID: " + transactionID);
        return transactionID;
    }

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


    ///////// SIMULATED USER BEHAVIOUR

    const generate = await runExec(`${defCommand} generate 1`);

    let transID = await setUpUser("SI");


    transID = await travel("SI", "CR", transID);
    transID = await travel("CR", "BiH", transID);
    transID = await travel("BiH", "RS", transID);
    transID = await travel("RS", "MAC", transID);
    transID = await travel("MAC", "GRE", transID);
    transID = await travel("GRE", "CYP", transID);
console.log("User 1:" + transID);


   let transID2 = await setUpUser("RUS");

    transID2 = await travel("RUS", "POL", transID2);
    transID2 = await travel("POL", "GER", transID2);
    transID2 = await travel("GER", "FR", transID2);
    transID2 = await travel("FR", "UK", transID2);
    transID2 = await travel("UK", "US", transID2);
    transID2 = await travel("US", "CAN", transID2);
console.log("User 1:" + transID2);
   let transID3 = await setUpUser("FR");

    transID3 = await travel("FR", "GER", transID3);
    transID3 = await travel("GER", "DEN", transID3);
    transID3 = await travel("DEN", "SWE", transID3);
    transID3 = await travel("SWE", "NOR", transID3);
console.log("User 1:" + transID3);


    // UPDATE USERS CARD WITH transID

    //let pathArr = await readPath(transID, []);
    //console.log(pathArr);
    ///// SEND pathArr TO FRONTEND














}

function runExec(command) {
    return new Promise(function (resolve, reject) {
        exec(command, (error, stdout, stderr) => {
            if(!error) resolve(stdout.replace("\n", ""));
            else reject(error);
        });
    });
}

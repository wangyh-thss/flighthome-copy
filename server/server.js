var express = require("express")
var bodyParser = require('body-parser');
var app = express()
const mongoose = require('mongoose');
const cityDB = mongoose.createConnection('mongodb://localhost:27017/cityDB')
const airportDB = mongoose.createConnection('mongodb://localhost:27017/airportDB')
const port = 8000;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


/*const {MongoClient} = require("mongodb")
uri = "mongodb://localhost:27017"
const client = new MongoClient(uri)*/

app.use(express.json())

const citySchema = new mongoose.Schema({
    cityid:String,
    iata:String
  })

const City = cityDB.model('City', citySchema);

const airportSchema = new mongoose.Schema({
    airportname:String,
    airportiata:String,
    cityiata:String
  })

const Airport = airportDB.model('Airport', airportSchema);


const server = app.listen(port, () => {
    console.log(`App listening on port ${port}`)
})

//start the server
app.get('/',(req, res) => {
    console.log('server is being accessed')
    res.send('server is active')
})

//save the cities
app.post("/city",(req,res)=>{
    var data = req.body
    data.forEach(element => {
        var cityid = element.nameCity
        var iata = element.codeIataCity
        console.log(cityid)
        console.log(iata)
        const newCity = new City({cityid,iata})
        newCity.save(err => err && console.log(err))
        /*const oldCity = City.findOne({cityid:cityid})
        if(!oldCity){
            if(iata==null){
                iata = ""
            }
            const newCity = new City({cityid,iata})
            newCity.save(err => err && console.log(err))
        }*/
    });
    res.status(200).json("sucess")
})

//get city iata
app.route("/city/:cityID").get((req,res,next) => {
    console.log(req.params.cityID)
    City.findOne({cityid: req.params.cityID},(err,city) => {
      if (err) return next(err);
      if (!city) return res.status(404).end('city not found');
      return res.json(city);
    })
  })

  app.post("/airport",(req,res)=>{
    var data = req.body
    data.forEach(element => {
        var airportname = element.nameAirport
        var airportiata = element.codeIataAirport
        var cityiata = element.codeIataCity
        console.log(airportname)
        console.log(airportiata)
        const newAirport = new Airport({airportname,airportiata,cityiata})
        newAirport.save(err => err && console.log(err))
    });
    res.status(200).json("sucess")
})

//get city iata
app.route("/airport/:cityiata").get((req,res,next) => {
    console.log(req.params.cityiata)
    Airport.find({cityiata: req.params.cityiata},(err,airport) => {
      if (err) return next(err);
      if (!airport||airport==[]) return res.status(404).end('airport not found');
      return res.json(airport);
    })
  })
package com.example.cartaicteproject.Adopter

 class UploadData
{
   lateinit var uriForImage:String
    lateinit var amountOfProduct:String
    constructor(){}
  constructor(uriMap:String,amountOfId:String)
  {   uriForImage=uriMap
      amountOfProduct=amountOfId


  }


 fun path():String
    {
        return uriForImage
    }

fun amountOfGood():String{
    return amountOfProduct
}
}
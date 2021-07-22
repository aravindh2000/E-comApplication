package com.example.cartaicteproject.Adopter

class ImageData
{
    var imageUrl:String=" "
    var name:String=" "
    constructor(){}
    constructor(imageUrl:String,name:String)
    {
        this.imageUrl=imageUrl
        this.name=name
    }
    fun imageUrl():String
    {
        return imageUrl
    }
    fun users():String
    {
        return name

    }
}
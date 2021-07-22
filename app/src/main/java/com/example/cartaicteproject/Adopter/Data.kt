package com.example.cartaicteproject.Adopter

class Data
{
    private var name:String=" "
    private  var uid:String=" "
    private var email:String=" "
    constructor()
    constructor(name:String,uid:String,email:String)
    {
        this.name=name
        this.uid=uid
        this.email=email
    }
    public fun getName():String{
        return name
    }
    public fun getUid():String
    {
        return uid
    }
    fun getEmail():String
    {
        return email
    }


}
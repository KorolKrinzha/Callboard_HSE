class User(val id:String, val username:String, val fullname:String){
    constructor(): this ("","", "")

    override fun toString(): String {
        return ("${id}! Username is ${username} + Fullname is ${fullname}")
    }
}
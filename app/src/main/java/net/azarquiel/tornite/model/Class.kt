package net.azarquiel.tornite.model

import java.io.Serializable

/**
 * Created by diego10mf on 25/05/20.
 */
data class Participante( var nick: String, var equipo: String )


data class Torneo(var id: String, var titulo: String = "", var fecha: String = "", var comienzo: String = "", var premio: String = ""):Serializable

data class Cod(var partida:String="", var codigo: String="")
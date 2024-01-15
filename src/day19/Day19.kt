package day19

import utils.Resources.resourceAsText

typealias Workflows = Map<String, List<Rule>>

fun main(){

    val input = resourceAsText("src/day19/Day19.txt")
    val parts = input.substringAfter("\n\n").split("\n").map { Part.parsePart(it) }

    fun parseWorkflow(input: String): Map<String, List<Rule>> {
        val workflows = emptyMap<String, List<Rule>>().toMutableMap()
        input.split("\n").forEach {
            workflows[it.substringBefore("{")] = it.substringAfter("{").substringBefore("}").split(",").map { rule -> Rule.parseRule(rule) }
        }
        return workflows
    }

    fun runWorkflow(part: Part, workflow: List<Rule>): String{
        return workflow.first{ rule -> rule.applyRule(part) }.output
    }

    val workflows = parseWorkflow(input.substringBefore("\n\n"))

    fun acceptOrReject(part: Part): Boolean{
        var current = workflows["in"]
        while (true){
            val output = current?.let { runWorkflow(part, it) }
            when(output){
                "A" -> return true
                "R" -> return false
                else -> current = workflows[output]
            }
        }
    }
    //fun countRanges(identifier: String, allWorkflows: Workflows, ranges: Map<Char,IntRange>): Long =
    //    when(identifier){
    //        "R" -> 0
    //        "A" -> ranges.values.map { it.length().toLong() }.reduce(Long::times)
    //        else -> {
    //            val rules = allWorkflows[identifier]
    //            val constrainedRanges = ranges.toMutableMap()
    //            rules
    //                ?.map{ it to allWorkflows.getValue(it.output)  }
    //                .sumOf { (rule, workflow) ->
    //                    when (rule){
//
    //                    }
    //                }
    //        }
    //    }
//
    fun part1():Int{
        return parts.filter { part -> acceptOrReject(part) }.sumOf { it.sum() }
    }

    println(part1())
}
data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
){
    companion object{
        fun parsePart(input: String) = Part(
            x = input.substringAfter("x=").substringBefore(",m").toInt(),
            m = input.substringAfter("m=").substringBefore(",a").toInt(),
            a = input.substringAfter("a=").substringBefore(",s").toInt(),
            s = input.substringAfter("s=").substringBefore("}").toInt()
        )

    }
    fun sum():Int{
        return this.x+this.m+this.a+this.s
    }
}
data class Rule(
    val identifier: String?,
    val operand: Char?,
    val rating: Int?,
    val output: String
){
    companion object{
        fun parseRule(input: String): Rule{
            return when(input.length >3 ){
                true -> Rule(
                    identifier = input.first().toString(),
                    operand = input[1],
                    rating = input.substring(2).substringBefore(":").toInt(),
                    output = input.substringAfter(":")
                )
                else -> Rule(null, null, null, input)
            }
        }
    }
    fun applyRule(part: Part): Boolean{
       if(this.identifier == null){
           return true
       }
        val partRating = when(this.identifier){
            "x" -> part.x
            "m" -> part.m
            "a" -> part.a
            else -> part.s
        }
        return when(this.operand){
            '<' -> partRating < this.rating!!
            else -> partRating > this.rating!!
        }
    }
}



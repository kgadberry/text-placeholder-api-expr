package mhl.papiexpr

import kotlin.math.PI
import kotlin.math.round
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.minecraft.text.Text
import eu.pb4.placeholders.api.Placeholders
import eu.pb4.placeholders.api.PlaceholderResult
import eu.pb4.placeholders.api.PlaceholderContext
import com.notkamui.keval.Keval

object PlaceholderAPIExpr: ModInitializer {
	private var _Keval: com.notkamui.keval.Keval = Keval.create {
		includeDefault()
		function {
			name = "mod"
			arity = 2
			implementation = { args: DoubleArray -> args[0] % args[1] }
		}
		function {
			name = "rad"
			arity = 1
			implementation = { args: DoubleArray -> PI / 180 * args[0] }
		}
		function {
			name = "max"
			implementation = { args: DoubleArray -> args.max() }
		}
		function {
			name = "min"
			implementation = { args: DoubleArray -> args.min() }
		}
		function {
			name = "avg"
			implementation = { args: DoubleArray -> args.average() }
		}
	}

	private fun kMath(str: String): String
	{
		val res: Double = _Keval.eval(str)
		if (round(res) == res)
			return (res.toInt().toString())
		else
			return (res.toString())
	}

	private fun parseNested(arg: String, ctx: PlaceholderContext): String
	{
		return (Placeholders.parseText(Text.literal(arg), ctx, Placeholders.PREDEFINED_PLACEHOLDER_PATTERN).getString())
	}

	private fun parsekMathOrString(isMath: String, arg: String): String
	{
		if (isMath.trim() == "true")
			return (kMath(arg))
		return (arg.trim())
	}

	override fun onInitialize()
	{
		Placeholders.register(Identifier.of("expr", "math")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing arguments")
				PlaceholderResult.value(Text.literal(kMath(parseNested(arg, ctx))))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "ifeq")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing arguments")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 8)
					throw Exception("Must have exactly 8 arguments")
				
				val a: String =
					if (args[0].trim(' ') == "true")
						kMath(args[1]) else args[1].trim()
				val b: String =
					if (args[2].trim(' ') == "true")
						kMath(args[3]) else args[3].trim()
				
				if (a == b)
					PlaceholderResult.value(Text.literal(parsekMathOrString(args[4], args[5])))
				else
					PlaceholderResult.value(Text.literal(parsekMathOrString(args[6], args[7])))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "iflt")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing arguments")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 6)
					throw Exception("Must have exactly 6 arguments")
				if (_Keval.eval(args[0]) < _Keval.eval(args[1]))
					PlaceholderResult.value(Text.literal(parsekMathOrString(args[2], args[3])))
				else
					PlaceholderResult.value(Text.literal(parsekMathOrString(args[4], args[5])))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "ifgt")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing arguments")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 6)
					throw Exception("Must have exactly 6 arguments")
				if (_Keval.eval(args[0]) > _Keval.eval(args[1]))
					PlaceholderResult.value(Text.literal(parsekMathOrString(args[2], args[3])))
				else
					PlaceholderResult.value(Text.literal(parsekMathOrString(args[4], args[5])))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "pad")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing argument")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 3)
					throw Exception("Must have exactly 3 arguments")
				val padChar: Char = args[2].trim().firstOrNull() ?: ' '
				val padCount: Int = ((_Keval.eval(args[0]).toInt() - args[1].trim().length) / 2).coerceAtLeast(0)
				val padString: String = padChar.toString().repeat(padCount)
				PlaceholderResult.value(Text.literal(padString + args[1].trim() + padString))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "padleft")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing argument")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 3)
					throw Exception("Must have exactly 3 arguments")
				val padChar: Char = args[2].trim().firstOrNull() ?: ' '
				val padCount: Int = (_Keval.eval(args[0]).toInt() - args[1].trim().length).coerceAtLeast(0)
				val padString: String = padChar.toString().repeat(padCount)
				PlaceholderResult.value(Text.literal(padString + args[1].trim()))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "padright")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing argument")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 3)
					throw Exception("Must have exactly 3 arguments")
				val padChar: Char = args[2].trim().firstOrNull() ?: ' '
				val padCount: Int = (_Keval.eval(args[0]).toInt() - args[1].trim().length).coerceAtLeast(0)
				val padString: String = padChar.toString().repeat(padCount)
				PlaceholderResult.value(Text.literal(args[1].trim() + padString))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "padmatch")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing argument")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 3)
					throw Exception("Must have exactly 3 arguments")
				val padChar: Char = args[2].trim().firstOrNull() ?: ' '
				val padCount: Int = ((args[0].trim().length - args[1].trim().length) / 2).coerceAtLeast(0)
				val padString: String = padChar.toString().repeat(padCount)
				PlaceholderResult.value(Text.literal(padString + args[1].trim() + padString))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "padmatchleft")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing argument")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 3)
					throw Exception("Must have exactly 3 arguments")
				val padChar: Char = args[2].trim().firstOrNull() ?: ' '
				val padCount: Int = (args[0].trim().length - args[1].trim().length).coerceAtLeast(0)
				val padString: String = padChar.toString().repeat(padCount)
				PlaceholderResult.value(Text.literal(padString + args[1].trim()))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}

		Placeholders.register(Identifier.of("expr", "padmatchright")) {ctx, arg ->
			try {
				if (arg == null)
					throw Exception("Missing argument")

				val args: List<String> = parseNested(arg, ctx).split(';')
				if (args.size != 3)
					throw Exception("Must have exactly 3 arguments")
				val padChar: Char = args[2].trim().firstOrNull() ?: ' '
				val padCount: Int = (args[0].trim().length - args[1].trim().length).coerceAtLeast(0)
				val padString: String = padChar.toString().repeat(padCount)
				PlaceholderResult.value(Text.literal(args[1].trim() + padString))
			}
			catch (e: Exception) {
				PlaceholderResult.invalid(e.message)
			}
		}
	}
}
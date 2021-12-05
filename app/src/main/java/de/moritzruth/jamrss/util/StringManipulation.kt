package de.moritzruth.jamrss.util

fun collapseMultipleLineBreaks(text: String): String {
    // I'm very proud of this
    return text.replace(Regex("\n(\n)+"), "\n")
}

fun rewriteWithoutLineBreaks(text: String): String {
    return text.split("\n").joinToString(" ") {
        if (it.endsWith(".") || it.endsWith(":")) it
        else "$it:"
    }
}

fun truncateWithEllipsis(maxLength: Int, text: String) =
    if (text.length > maxLength) text.take(maxLength - 1).trimEnd() + "…"
    else text
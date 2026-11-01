package com.masum.devfolio.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val blocks = remember(markdown) { parseMarkdownBlocks(markdown) }

    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Header -> {
                    val (style, topPadding) = when (block.level) {
                        1 -> MaterialTheme.typography.titleLarge to 16.dp
                        2 -> MaterialTheme.typography.titleMedium to 12.dp
                        else -> MaterialTheme.typography.titleSmall to 8.dp
                    }
                    Spacer(modifier = Modifier.height(topPadding))
                    Text(
                        text = block.content,
                        style = style,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                is MarkdownBlock.Bullet -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = parseInlineMarkdown(block.content),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                is MarkdownBlock.Paragraph -> {
                    Text(
                        text = parseInlineMarkdown(block.content),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

private sealed interface MarkdownBlock {
    data class Header(val level: Int, val content: AnnotatedString) : MarkdownBlock
    data class Bullet(val content: String) : MarkdownBlock
    data class Paragraph(val content: String) : MarkdownBlock
}

private fun parseMarkdownBlocks(text: String): List<MarkdownBlock> {
    val result = mutableListOf<MarkdownBlock>()
    val lines = text.split("\n")

    for (line in lines) {
        val trimmed = line.trim()
        if (trimmed.isEmpty()) continue

        when {
            trimmed.startsWith("### ") -> {
                result.add(MarkdownBlock.Header(3, parseInlineMarkdown(trimmed.substring(4))))
            }
            trimmed.startsWith("## ") -> {
                result.add(MarkdownBlock.Header(2, parseInlineMarkdown(trimmed.substring(3))))
            }
            trimmed.startsWith("# ") -> {
                result.add(MarkdownBlock.Header(1, parseInlineMarkdown(trimmed.substring(2))))
            }
            trimmed.startsWith("- ") || trimmed.startsWith("* ") -> {
                result.add(MarkdownBlock.Bullet(trimmed.substring(2)))
            }
            else -> {
                result.add(MarkdownBlock.Paragraph(line))
            }
        }
    }

    return result
}

private fun parseInlineMarkdown(text: String): AnnotatedString {
    val regex = Regex("""(\*\*([^*]+)\*\*|\*([^*]+)\*|`([^`]+)`)""")
    var lastIndex = 0

    return buildAnnotatedString {
        for (match in regex.findAll(text)) {
            val start = match.range.first
            val end = match.range.last + 1

            if (start > lastIndex) {
                append(text.substring(lastIndex, start))
            }

            val bold = match.groups[2]?.value
            val italic = match.groups[3]?.value
            val code = match.groups[4]?.value

            when {
                bold != null -> {
                    val prevLen = length
                    append(bold)
                    addStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold),
                        start = prevLen,
                        end = length
                    )
                }
                italic != null -> {
                    val prevLen = length
                    append(italic)
                    addStyle(
                        style = SpanStyle(fontStyle = FontStyle.Italic),
                        start = prevLen,
                        end = length
                    )
                }
                code != null -> {
                    val prevLen = length
                    append(code)
                    addStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium
                        ),
                        start = prevLen,
                        end = length
                    )
                }
            }

            lastIndex = end
        }

        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}

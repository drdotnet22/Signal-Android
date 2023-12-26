/*
 * Copyright 2023 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.thoughtcrime.securesms.components.webrtc

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.components.emoji.EmojiImageView
import org.thoughtcrime.securesms.keyvalue.SignalStore
import org.thoughtcrime.securesms.reactions.any.ReactWithAnyEmojiBottomSheetDialogFragment

class CallReactionScrubber @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  private val emojiViews: Array<EmojiImageView>
  private var customEmojiIndex = 0

  init {
    inflate(context, R.layout.call_overflow_popup, this)

    emojiViews = arrayOf(
      findViewById(R.id.reaction_1),
      findViewById(R.id.reaction_2),
      findViewById(R.id.reaction_3),
      findViewById(R.id.reaction_4),
      findViewById(R.id.reaction_5),
      findViewById(R.id.reaction_6),
      findViewById(R.id.reaction_7)
    )
    customEmojiIndex = emojiViews.size - 1
  }

  fun initialize(fragmentManager: FragmentManager, callback: ReactWithAnyEmojiBottomSheetDialogFragment.Callback, listener: (String) -> Unit) {
    val emojis = SignalStore.emojiValues().reactions
    for (i in emojiViews.indices) {
      val view = emojiViews[i]
      val isAtCustomIndex = i == customEmojiIndex
      if (isAtCustomIndex) {
        view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_any_emoji_32))
        view.setOnClickListener {
          val bottomSheet = ReactWithAnyEmojiBottomSheetDialogFragment.createForCallingReactions()
          bottomSheet.show(fragmentManager, "CallReaction")
        }
      } else {
        val preferredVariation = SignalStore.emojiValues().getPreferredVariation(emojis[i])
        view.setImageEmoji(preferredVariation)
        view.setOnClickListener { listener(preferredVariation) }
      }
    }
  }
}

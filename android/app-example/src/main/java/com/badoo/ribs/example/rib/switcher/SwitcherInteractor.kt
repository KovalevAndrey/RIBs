package com.badoo.ribs.example.rib.switcher

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.switcher.SwitcherView.Event
import com.badoo.ribs.example.rib.switcher.dialog.DialogToTestOverlay
import io.reactivex.functions.Consumer

class SwitcherInteractor(
    savedInstanceState: Bundle?,
    private val router: Router<Configuration, Permanent, Content, Overlay, SwitcherView>,
    private val dialogToTestOverlay: DialogToTestOverlay
) : Interactor<SwitcherView>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {

    override fun onViewCreated(view: SwitcherView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.startStop {
            bind(view to viewEventConsumer)
            bind(dialogToTestOverlay to dialogEventConsumer)
        }
    }

    internal inner class MenuListener : Consumer<Menu.Output> {
        override fun accept(output: Menu.Output) = when (output) {
            is Menu.Output.MenuItemSelected -> when (output.menuItem) {
                Menu.MenuItem.FooBar -> {
                    router.replace(Content.Foo)
                }
                Menu.MenuItem.HelloWorld -> {
                    router.replace(Content.Hello)
                }
                Menu.MenuItem.Dialogs -> {
                    router.replace(Content.DialogsExample)
                }
            }
        }
    }

    private val viewEventConsumer: Consumer<Event> = Consumer {
        when (it) {
            Event.ShowOverlayDialogClicked -> router.pushOverlay(Overlay.Dialog)
            Event.ShowBlockerClicked -> router.push(Content.Blocker)
        }
    }

    internal val loremIpsumOutputConsumer: Consumer<Blocker.Output> = Consumer {
        // Clear Blocker
        router.popBackStack()
    }

    private val dialogEventConsumer : Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                // do something if you want
            }
        }
    }
}

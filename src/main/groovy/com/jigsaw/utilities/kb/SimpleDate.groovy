package com.jigsaw.utilities.kb

import org.kohsuke.stapler.DataBoundConstructor

class SimpleDate extends Date {
    @DataBoundConstructor
    SimpleDate() {
        super()
    }
}

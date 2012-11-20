/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.modeling.jgen.snippets;

import java.io.IOException;

import org.slc.sli.modeling.jgen.JavaParam;
import org.slc.sli.modeling.jgen.JavaSnippet;
import org.slc.sli.modeling.jgen.JavaSnippetExpr;
import org.slc.sli.modeling.jgen.JavaStreamWriter;

public final class EnhancedForLoop implements JavaSnippet {

    private final JavaParam param;
    private final JavaSnippetExpr rhs;
    private final JavaSnippet body;

    public EnhancedForLoop(final JavaParam param, final JavaSnippetExpr rhs, final JavaSnippet body) {
        if (param == null) {
            throw new IllegalArgumentException("param");
        }
        if (rhs == null) {
            throw new IllegalArgumentException("rhs");
        }
        if (body == null) {
            throw new IllegalArgumentException("body");
        }
        this.param = param;
        this.rhs = rhs;
        this.body = body;
    }

    @Override
    public void write(final JavaStreamWriter jsw) throws IOException {
        if (jsw == null) {
            throw new IllegalArgumentException("jsw");
        }
        jsw.write("for");
        jsw.space();
        jsw.parenL();
        try {
            jsw.write("final").space().writeType(param.getType()).space().write(param.getName()).space().write(":")
                    .space().write(rhs);
        } finally {
            jsw.parenR();
        }
        jsw.beginBlock();
        try {
            jsw.write(body);
        } finally {
            jsw.endBlock();
        }
    }
}

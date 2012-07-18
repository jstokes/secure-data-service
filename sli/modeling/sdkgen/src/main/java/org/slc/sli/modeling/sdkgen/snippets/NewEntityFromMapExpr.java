package org.slc.sli.modeling.sdkgen.snippets;

import java.io.IOException;

import org.slc.sli.modeling.jgen.JavaSnippetExpr;
import org.slc.sli.modeling.jgen.JavaStreamWriter;
import org.slc.sli.modeling.jgen.JavaType;
import org.slc.sli.modeling.jgen.snippets.NewInstanceExpr;
import org.slc.sli.modeling.jgen.snippets.StringLiteralExpr;
import org.slc.sli.modeling.jgen.snippets.VarNameExpr;

public final class NewEntityFromMapExpr implements JavaSnippetExpr {

    private final JavaSnippetExpr rhs;

    public NewEntityFromMapExpr(final JavaType type, final String name) {
        rhs = new NewInstanceExpr(type, new StringLiteralExpr(name), new VarNameExpr(name));
    }

    @Override
    public String toString() {
        return rhs.toString();
    }

    @Override
    public void write(final JavaStreamWriter jsw) throws IOException {
        if (jsw == null) {
            throw new NullPointerException("jsw");
        }
        jsw.write(rhs);
    }

}

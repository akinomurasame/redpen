/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
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
package cc.redpen.tokenizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TokenElement implements Serializable {
    private static final long serialVersionUID = -7779529873101010570L;
    final private String surface;

    final private List<String> tags;

    TokenElement(String word) {
        surface = word;
        tags = Collections.unmodifiableList(new ArrayList<>());
    }

    TokenElement(String word, String tag) {
        surface = word;
        tags = Collections.unmodifiableList(Arrays.asList(tag));
    }

    TokenElement(String word, List<String> tagList) {
        surface = word;
        tags = Collections.unmodifiableList(tagList);
    }

    public String getSurface() {
        return surface;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenElement that = (TokenElement) o;

        if (surface != null ? !surface.equals(that.surface) : that.surface != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = surface != null ? surface.hashCode() : 0;
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TokenElement{" +
                "surface='" + surface + '\'' +
                ", tags=" + tags +
                '}';
    }
}

/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.api.reaserch.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ResearchCriteriaContainer {
    public static final Codec<ResearchCriteriaContainer> CODEC = RecordCodecBuilder.create(i -> i.group(
            ConfiguredResearchCriteria.LIST_REGISTRY_CODEC.listOf().fieldOf("criteria").forGetter(ResearchCriteriaContainer::getCriteria)
    ).apply(i, ResearchCriteriaContainer::new));

    public static final ResearchCriteriaContainer EMPTY = new ResearchCriteriaContainer(Collections.emptyList());
    private final List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> criteria;

    public ResearchCriteriaContainer(List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> criteria) {
        this.criteria = criteria;
    }

    public List<List<Supplier<ConfiguredResearchCriteria<?, ?>>>> getCriteria() {
        return criteria;
    }
}

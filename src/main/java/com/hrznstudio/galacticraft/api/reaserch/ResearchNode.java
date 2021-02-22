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

package com.hrznstudio.galacticraft.api.reaserch;

import com.hrznstudio.galacticraft.api.internal.codec.LazyRegistryElementCodec;
import com.hrznstudio.galacticraft.api.reaserch.criteria.ResearchCriteriaContainer;
import com.hrznstudio.galacticraft.api.reaserch.reward.ResearchRewardContainer;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import java.util.*;
import java.util.function.Supplier;

public class ResearchNode {
    public static final Codec<Supplier<ResearchNode>> REGISTRY_CODEC = LazyRegistryElementCodec.of(AddonRegistry.RESEARCH_NODE_KEY, new Lazy<>(() -> ResearchNode.CODEC));
    public static final Codec<ResearchNode> CODEC = RecordCodecBuilder.create(i ->
            i.group(Identifier.CODEC.fieldOf("id").forGetter(ResearchNode::getId),
                    ResearchNode.REGISTRY_CODEC.listOf().optionalFieldOf("parents").forGetter(node -> {
                        List<Supplier<ResearchNode>> list = new ArrayList<>(node.getParents().size());
                        for (Lazy<ResearchNode> researchNodeLazy : node.getParents()) {
                            list.add(researchNodeLazy::get);
                        }
                        return list.isEmpty() ? Optional.empty() : Optional.of(list);
                    }),
                    ResearchNodeDisplay.CODEC.fieldOf("display").forGetter(ResearchNode::getDisplay),
                    ResearchCriteriaContainer.CODEC.fieldOf("criteria").forGetter(ResearchNode::getCriteriaContainer),
                    ResearchRewardContainer.CODEC.fieldOf("rewards").forGetter(ResearchNode::getRewardContainer)).apply(i, ResearchNode::new));

    private final Identifier id;
    private final List<Lazy<ResearchNode>> parents;
    private final ResearchCriteriaContainer criteriaContainer;
    private final ResearchNodeDisplay display;
    private final ResearchRewardContainer rewardContainer;

    private ResearchNode(Identifier id, Optional<List<Supplier<ResearchNode>>> parents, ResearchNodeDisplay display, ResearchCriteriaContainer criteriaContainer, ResearchRewardContainer rewardContainer) {
        this.id = id;
        if (parents.isPresent()) {
            List<Lazy<ResearchNode>> list = new ArrayList<>(parents.get().size());
            for (Supplier<ResearchNode> parent : parents.get()) {
                list.add(new Lazy<>(parent));
            }
            this.parents = list;
        } else {
            this.parents = Collections.emptyList();
        }
        this.criteriaContainer = criteriaContainer;
        this.display = display;
        this.rewardContainer = rewardContainer;
    }

    private ResearchNode(Identifier id, List<Lazy<ResearchNode>> parents, ResearchCriteriaContainer criteriaContainer, ResearchNodeDisplay display, ResearchRewardContainer rewardContainer) {
        this.id = id;
        this.parents = parents;
        this.criteriaContainer = criteriaContainer;
        this.display = display;
        this.rewardContainer = rewardContainer;
    }

    public Identifier getId() {
        return id;
    }

    public List<Lazy<ResearchNode>> getParents() {
        return parents;
    }

    public ResearchCriteriaContainer getCriteriaContainer() {
        return criteriaContainer;
    }

    public ResearchNodeDisplay getDisplay() {
        return display;
    }

    public ResearchRewardContainer getRewardContainer() {
        return rewardContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchNode that = (ResearchNode) o;
        return getId().equals(that.getId())
                && getParents().equals(that.getParents())
                && getCriteriaContainer().equals(that.getCriteriaContainer())
                && getDisplay().equals(that.getDisplay())
                && getRewardContainer().equals(that.getRewardContainer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getParents(), getCriteriaContainer(), getDisplay(), getRewardContainer());
    }

    @Override
    public String toString() {
        return "ResearchNode{" +
                "id=" + id +
                ", parents=" + parents +
                ", criteriaContainer=" + criteriaContainer +
                ", display=" + display +
                ", rewardContainer=" + rewardContainer +
                '}';
    }

    public static final class Builder {
        private Identifier id;
        private List<Lazy<ResearchNode>> parents = new ArrayList<>();
        private ResearchCriteriaContainer criteriaContainer = ResearchCriteriaContainer.EMPTY;
        private ResearchNodeDisplay display = ResearchNodeDisplay.DEFAULT;
        private ResearchRewardContainer rewardContainer = ResearchRewardContainer.EMPTY;

        public Builder(Identifier id) {
            this.id = id;
        }

        public static Builder create(Identifier id) {
            return new Builder(id);
        }

        public Builder id(Identifier id) {
            this.id = id;
            return this;
        }

        public Builder parents(List<Lazy<ResearchNode>> parents) {
            this.parents = parents;
            return this;
        }

        public Builder criteria(ResearchCriteriaContainer criteria) {
            this.criteriaContainer = criteria;
            return this;
        }

        public Builder display(ResearchNodeDisplay display) {
            this.display = display;
            return this;
        }

        public Builder rewards(ResearchRewardContainer rewards) {
            this.rewardContainer = rewards;
            return this;
        }

        public ResearchNode build() {
            assert this.id != null;
            assert this.display != null;
            assert this.criteriaContainer != null;
            return new ResearchNode(this.id, this.parents, this.criteriaContainer, this.display, this.rewardContainer);
        }
    }

}

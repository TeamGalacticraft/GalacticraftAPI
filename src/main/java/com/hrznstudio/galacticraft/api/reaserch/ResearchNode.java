package com.hrznstudio.galacticraft.api.reaserch;

import com.hrznstudio.galacticraft.api.internal.codec.LazyRegistryElementCodec;
import com.hrznstudio.galacticraft.api.reaserch.criteria.ResearchCriteriaContainer;
import com.hrznstudio.galacticraft.api.reaserch.reward.ResearchRewardContainer;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ResearchNode {
    public static final Codec<Supplier<ResearchNode>> REGISTRY_CODEC = LazyRegistryElementCodec.of(AddonRegistry.RESEARCH_NODE_KEY, new Lazy<>(() -> ResearchNode.CODEC));
    public static final Codec<ResearchNode> CODEC = RecordCodecBuilder.create(i ->
            i.group(Identifier.CODEC.fieldOf("id").forGetter(ResearchNode::getId),
                    ResearchNode.REGISTRY_CODEC.listOf().fieldOf("parents").forGetter(node -> {
                        List<Supplier<ResearchNode>> list = new ArrayList<>(node.getParents().size());
                        for (Lazy<ResearchNode> researchNodeLazy : node.getParents()) {
                            list.add(researchNodeLazy::get);
                        }
                        return list;
                    }),
                    ResearchNodeDisplay.CODEC.fieldOf("display").forGetter(ResearchNode::getDisplay),
                    ResearchCriteriaContainer.CODEC.fieldOf("criteria").forGetter(ResearchNode::getCriteriaContainer),
                    ResearchRewardContainer.CODEC.fieldOf("rewards").forGetter(ResearchNode::getRewardContainer)).apply(i, ResearchNode::new));

    private final Identifier id;
    private final List<Lazy<ResearchNode>> parents;
    private final ResearchCriteriaContainer criteriaContainer;
    private final ResearchNodeDisplay display;
    private final ResearchRewardContainer rewardContainer;

    private ResearchNode(Identifier id, List<Supplier<ResearchNode>> parents, ResearchNodeDisplay display, ResearchCriteriaContainer criteriaContainer, ResearchRewardContainer rewardContainer) {
        this.id = id;
        List<Lazy<ResearchNode>> list = new ArrayList<>(parents.size());
        for (Supplier<ResearchNode> parent : parents) {
            list.add(new Lazy<>(parent));
        }
        this.parents = list;
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

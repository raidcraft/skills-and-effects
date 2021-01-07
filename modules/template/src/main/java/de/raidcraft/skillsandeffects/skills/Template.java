package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.configuration.ConfigurationSection;

// Steps to get you started:
// 1. name the class to match your skill
// 2. adjust the log topic and skill identifier
// 3. add primitive config values as properties tagged with @ConfigOption and load the rest inside the load(...) method
// 4. (optional) implement listener if you require any bukkit events
// 5. (optional) implement Executable if your skill can be actively executed by the player
// 6. (recommended) add unit tests to test the logic of your skill

@Log(topic = "RCSkills:template")
@SkillInfo(
        value = "template",
        depends = {
                // add plugin dependencies of the skill here
        }
)
public class Template extends AbstractSkill {

    public static class Factory implements SkillFactory<Template> {

        @Override
        public Class<Template> getSkillClass() {
            return Template.class;
        }
        @Override
        public @NonNull Template create(SkillContext context) {
            return new Template(context);
        }
    }

    protected Template(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {
        //TODO: implement
    }

    @Override
    public void apply() {
        //TODO: implement
    }

    @Override
    public void remove() {
        //TODO: implement
    }
}

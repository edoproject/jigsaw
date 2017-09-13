package com.jigsaw.utilities.kb

import com.sonyericsson.jenkins.plugins.bfa.Messages
import com.sonyericsson.jenkins.plugins.bfa.PluginImpl
import com.sonyericsson.jenkins.plugins.bfa.db.KnowledgeBase
import com.sonyericsson.jenkins.plugins.bfa.db.KnowledgeBase.KnowledgeBaseDescriptor
import com.sonyericsson.jenkins.plugins.bfa.db.LocalFileKnowledgeBase
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCause
import com.sonyericsson.jenkins.plugins.bfa.statistics.Statistics
import hudson.Extension
import hudson.model.Descriptor
import hudson.model.Run
import hudson.util.CopyOnWriteList
import jenkins.model.Jenkins
import org.kohsuke.stapler.DataBoundConstructor

import static hudson.Util.fixEmpty

class SimpleKnowledgeBase extends KnowledgeBase {
    private Map<String, FailureCause> causes;

    /**
     * Standard constructor. Used for legacy conversion.
     *
     * @param legacyCauses the causes.
     */
    public SimpleKnowledgeBase(CopyOnWriteList<FailureCause> legacyCauses) {
        this(legacyCauses.getView());
    }

    /**
     * Standard constructor. Used for simple testability.
     *
     * @param initialCauses the causes.
     */
    @DataBoundConstructor
    public SimpleKnowledgeBase(Collection<FailureCause> initialCauses) {
        this.causes = new HashMap<String, FailureCause>();
        for (FailureCause cause : initialCauses) {
            if (fixEmpty(cause.getId()) == null) {
                cause.setId(UUID.randomUUID().toString());
            }
            causes.put(cause.getId(), cause);
        }
    }

    /**
     * Default constructor.
     */
    public SimpleKnowledgeBase() {
        causes = new HashMap<String, FailureCause>();
    }

    @Override
    public Collection<FailureCause> getCauses() {
        return causes.values();
    }

    @Override
    public Collection<FailureCause> getCauseNames() {
        return getCauses();
    }

    @Override
    public Collection<FailureCause> getShallowCauses() throws Exception {
        return getCauses();
    }

    @Override
    public FailureCause getCause(String id) {
        return causes.get(id);
    }

    @Override
    public FailureCause addCause(FailureCause cause) throws IOException {
        cause.setId(UUID.randomUUID().toString());
        causes.put(cause.getId(), cause);
        PluginImpl.getInstance().save();
        return cause;
    }

    @Override
    public FailureCause removeCause(String id) throws Exception {
        FailureCause remove = causes.remove(id);
        PluginImpl.getInstance().save();
        return remove;
    }

    @Override
    public FailureCause saveCause(FailureCause cause) throws IOException {
        if (fixEmpty(cause.getId()) == null) {
            return addCause(cause);
        } else {
            causes.put(cause.getId(), cause);
            PluginImpl.getInstance().save();
            return cause;
        }
    }

    /**
     * Puts the cause directly into the map. Does not call save.
     * @param cause the cause to put.
     */
    protected void put(FailureCause cause) {
        causes.put(cause.getId(), cause);
    }

    @Override
    public void convertFrom(KnowledgeBase oldKnowledgeBase) throws Exception {
        if (oldKnowledgeBase instanceof SimpleKnowledgeBase) {
            SimpleKnowledgeBase lfkb = (SimpleKnowledgeBase) oldKnowledgeBase;
            causes = lfkb.causes;
        } else {
            convertFromAbstract(oldKnowledgeBase);
        }
    }

    @Override
    public List<String> getCategories() throws Exception {
        if (causes == null) {
            return null;
        }
        List<String> categories = new LinkedList<String>();
        Set myset = new HashSet<String>();
        for (FailureCause cause : causes.values()) {
            List<String> categoriesForCause = cause.getCategories();
            if (categoriesForCause != null) {
                for (String string : categoriesForCause) {
                    if (myset.add(string)) {
                        categories.add(string);
                    }
                }
            }
        }
        return categories;
    }

    @Override
    public boolean equals(KnowledgeBase oldKnowledgeBase) {
        if (getClass().isInstance(oldKnowledgeBase)) {
            return oldKnowledgeBase.getClass().getName().equals(this.getClass().getName());
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        //TODO should something be done here?
    }

    @Override
    public void stop() {
        //TODO should something be done here?
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof KnowledgeBase) {
            return this.equals((KnowledgeBase) other);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        //Making checkstyle happy.
        return getClass().getName().hashCode();
    }

    @Override
    public boolean isStatisticsEnabled() {
        //Not implemented
        return false;
    }

    @Override
    public boolean isSuccessfulLoggingEnabled() {
        //not implemented
        return false;
    }

    @Override
    public void saveStatistics(Statistics stat) throws Exception {
        //Not implemented.
    }

    @Override
    public Descriptor<KnowledgeBase> getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(LocalFileKnowledgeBase.LocalFileKnowledgeBaseDescriptor.class);
    }

    @Override
    public void removeBuildfailurecause(Run build) throws Exception {
        //Not implemented
    }

    /**
     * Descriptor for {@link LocalFileKnowledgeBase}.
     */
    @Extension
    public static class LocalFileKnowledgeBaseDescriptor extends KnowledgeBaseDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.LocalFileKnowledgeBase_DisplayName();
        }
    }
}

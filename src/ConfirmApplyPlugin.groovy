import static TerraformEnvironmentStage.CONFIRM

class ConfirmApplyPlugin implements TerraformEnvironmentStagePlugin {

    public static enabled = true

    ConfirmApplyPlugin() {
    }

    public static void init() {
        TerraformEnvironmentStage.addPlugin(new ConfirmApplyPlugin())
    }

    @Override
    public void apply(TerraformEnvironmentStage stage) {
        if (enabled) {
            stage.decorate(CONFIRM, addConfirmation())
        }
    }

    public static Closure addConfirmation() {
        return { closure ->
            // ask for human input
            try {
                timeout(time: 15, unit: 'MINUTES') {
                    input(
                        message: 'Are you absolutely sure the plan above is correct, and should be IMMEDIATELY DEPLOYED via "terraform apply"?',
                        ok: 'Run terraform APPLY now',
                        submitterParameter: 'approver'
                    )
                }
            } catch (ex) {
                throw ex
            }
            closure()
        }
    }

    public static disable() {
        this.enabled = false
        return this
    }

    public static enable() {
        this.enabled = true
        return this
    }
}

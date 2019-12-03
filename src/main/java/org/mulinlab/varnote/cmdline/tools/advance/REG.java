package org.mulinlab.varnote.cmdline.tools.advance;

import org.broadinstitute.barclay.argparser.Argument;
import org.broadinstitute.barclay.argparser.ArgumentCollection;
import org.broadinstitute.barclay.argparser.CommandLineProgramProperties;
import org.mulinlab.varnote.cmdline.abstractclass.QueryFileProgram;
import org.mulinlab.varnote.cmdline.collection.OutputArgumentCollection;
import org.mulinlab.varnote.cmdline.collection.VariantEffectArgumentCollection;
import org.mulinlab.varnote.cmdline.programgroups.AdvanceProgramGroup;
import org.mulinlab.varnote.config.param.output.OutParam;
import org.mulinlab.varnote.config.param.query.QueryFileParam;
import org.mulinlab.varnote.config.run.CEPIPRunConfig;
import org.mulinlab.varnote.constants.GlobalParameter;
import org.mulinlab.varnote.utils.RunFactory;
import org.mulinlab.varnote.utils.enumset.CellType;
import org.mulinlab.varnote.utils.format.Format;

import java.util.List;

@CommandLineProgramProperties(
        summary = REG.USAGE_SUMMARY + REG.USAGE_DETAILS,
        oneLineSummary = REG.USAGE_SUMMARY,
        programGroup = AdvanceProgramGroup.class)

public final class REG extends QueryFileProgram { //CEPIP
    static final String USAGE_SUMMARY = "REG";
    static final String USAGE_DETAILS =
            "\n\nUsage example:" +
            "\n" +
            "java -jar " + GlobalParameter.PRO_NAME + ".jar REG -Q query.vcf\n" ;


    @ArgumentCollection()
    public final OutputArgumentCollection outputArguments = new OutputArgumentCollection();

    @ArgumentCollection()
    public final VariantEffectArgumentCollection variantEffectArgument = new VariantEffectArgumentCollection();

    @Argument( shortName = "ID", fullName = "CellID", optional = true,
            doc = "Cell type to extract, separated by comma. Please refer EID in https://github.com/mdozmorov/genomerunner_web/wiki/Roadmap-cell-types for valid Cell IDs"
    )
    public List<CellType> cellIDs;

    @Override
    protected int doWork() {
        Format format = getFormat();

        CEPIPRunConfig runConfig = new CEPIPRunConfig(new QueryFileParam(inputArguments.getQueryFilePath(), format, false), dbArguments.getDBList(), variantEffectArgument.getJannovarUtils(), cellIDs);
        runConfig.setOutParam(outputArguments.getOutParam(new OutParam()));
        runConfig.setThread(runArguments.getThreads());

        RunFactory.runCEPIP(runConfig);
        return -1;
    }
}

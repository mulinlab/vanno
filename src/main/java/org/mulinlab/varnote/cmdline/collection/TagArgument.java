package org.mulinlab.varnote.cmdline.collection;


import org.broadinstitute.barclay.argparser.TaggedArgument;
import org.mulinlab.varnote.config.param.DBParam;
import org.mulinlab.varnote.constants.GlobalParameter;
import org.mulinlab.varnote.exceptions.InvalidArgumentException;
import org.mulinlab.varnote.utils.VannoUtils;
import org.mulinlab.varnote.utils.enumset.Delimiter;
import org.mulinlab.varnote.utils.enumset.FormatType;
import org.mulinlab.varnote.utils.format.Format;
import java.util.HashMap;
import java.util.Map;

public final class TagArgument implements TaggedArgument {
    private String tagName;
    private Map<String, String> tagAttributes = new HashMap<>();
    public String argValue;

    public TagArgument(final String value) {
        this.argValue = value;
    }

    public TagArgument(final String value, final String tagName)
    {
        this(value);
        this.tagName = tagName;
    }

    @Override
    public void setTag(final String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getTag() {
        return tagName;
    }

    public String getArgValue() {
        return argValue;
    }

    @Override
    public void setTagAttributes(final Map<String, String> attributes) {
        this.tagAttributes.putAll(attributes);
    }

    @Override
    public Map<String, String> getTagAttributes() {
        return tagAttributes;
    }

    @Override
    public String toString() { return argValue; }

    public DBParam getDBParam() {
        DBParam dbParam = new DBParam(argValue);
        String index = tagAttributes.get("index");
        String mode = tagAttributes.get("mode");
        String tag = tagAttributes.get("tag");

        if(index != null) {
            dbParam.setIndexType(index);
        }

        if(mode != null) {
            dbParam.setIntersect(mode);
        }

        if(tag != null) {
            dbParam.setOutName(tag);
        }
        return dbParam;
    }

    public Format getFormat(boolean isVariant) {
        if(this.tagName == null || this.tagName.trim().equals("")) return null;
        Format format = VannoUtils.checkQueryFormat(this.tagName);
        if(format == null) {
            throw new InvalidArgumentException(String.format("Invalid tag name %s, possible tags are: {%s}", tagName, isVariant ? FormatType.varValues() : FormatType.allValues()));
        } else {
            return format;
        }
    }

    public Format setFormat(Format format) {
        if(format.type == FormatType.TAB) {
            String chrom = tagAttributes.get("c");
            String begin = tagAttributes.get("b");
            String end = tagAttributes.get("e");
            String ref = tagAttributes.get("ref");
            String alt = tagAttributes.get("alt");

            if(chrom != null) format.sequenceColumn = Integer.parseInt(chrom);
            if(begin != null) format.startPositionColumn = Integer.parseInt(begin);
            if(end != null) format.endPositionColumn = Integer.parseInt(end);
            if(ref != null) format.refPositionColumn = Integer.parseInt(ref);
            if(alt != null) format.altPositionColumn = Integer.parseInt(alt);

            String zeroBased = tagAttributes.get("0");
            if(zeroBased != null && VannoUtils.strToBool(zeroBased)) format.setZeroBased();
        }

        String commentIndicator = tagAttributes.get("ci");
        if(commentIndicator != null &&!commentIndicator.equals(GlobalParameter.DEFAULT_COMMENT_INDICATOR))
            format.setCommentIndicator(commentIndicator);

        String sep = tagAttributes.get("sep");
        if(sep != null) {
            format.setDelimiter(Delimiter.toDelimiter(sep));
        }

        return format;
    }
}



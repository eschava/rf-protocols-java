package rf.protocols.external.sdrtrunk;

import controller.ResourceManager;
import controller.channel.Channel;
import controller.channel.ProcessingChain;
import controller.site.Site;
import decode.DecoderType;
import decode.config.DecodeConfigFactory;
import decode.config.DecodeConfiguration;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.external.Adapter;
import rf.protocols.external.ognl.PropertiesConfigurer;
import sample.Listener;
import sample.complex.ComplexBuffer;
import source.SourceType;
import source.config.SourceConfigFactory;
import source.config.SourceConfigTuner;
import source.tuner.FrequencyChangeListener;
import source.tuner.Tuner;
import source.tuner.TunerChannelSource;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SDRTrunkAdapter implements Adapter {
    private boolean initialized = false;
    private SDRTrunkAdapterProperties properties = new SDRTrunkAdapterProperties();
    private PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);

    private Tuner initialize() {
        ResourceManager resourceManager = new ResourceManager();

        SourceConfigTuner sourceConfiguration = (SourceConfigTuner) SourceConfigFactory.getSourceConfiguration(SourceType.TUNER);
        sourceConfiguration.setFrequency(properties.frequency);

        DecodeConfiguration decodeConfiguration = DecodeConfigFactory.getDecodeConfiguration(DecoderType.NBFM);

        Channel channel = new Channel();
        channel.setEnabled(true);
        channel.setSite(new Site(), false);
        channel.setSourceConfiguration(sourceConfiguration);
        channel.setDecodeConfiguration(decodeConfiguration);
        channel.setResourceManager(resourceManager);

        ProcessingChain chain = channel.getProcessingChain();
        TunerChannelSource source = (TunerChannelSource) chain.getSource();
        return source.getTuner();
    }

    @Override
    public String getName() {
        return "sdrtrunk";
    }

    @Override
    public void setProperty(String name, String value) {
        propertiesConfigurer.setProperty(name, value);
    }

    @Override
    public void addListener(String pin, SignalLengthListener listener) {
        if (!initialized) {
            Tuner tuner = initialize();
            SDRTrunkListener bufferListener = new SDRTrunkListener(tuner, listener, properties.threshold);
            tuner.addListener((Listener<ComplexBuffer>) bufferListener);
            tuner.addListener((FrequencyChangeListener) bufferListener);
            initialized = true;
        }
    }

    @Override
    public void addListener(String pin, SignalLevelListener listener) {
        throw new UnsupportedOperationException("Signal level listeners are not supported");
    }

    @Override
    public SignalLengthSender getSignalSender(String pin) {
        throw new UnsupportedOperationException();
    }
}

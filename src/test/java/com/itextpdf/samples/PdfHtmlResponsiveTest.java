package com.itextpdf.samples;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.sandbox.pdfhtml.PdfHtmlResponsiveDesign;
import com.itextpdf.styledxmlparser.css.util.CssUtils;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Collection;

public class PdfHtmlResponsiveTest extends WrappedSamplesRunner {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.pdfhtml.PdfHtmlResponsiveDesign");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 120000)
    public void test() throws Exception {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/all-products.xml");
        FontProgramFactory.clearRegisteredFonts();

        runSamples();
        unloadLicense();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();

        for (int i = 0; i < PdfHtmlResponsiveDesign.pageSizes.length; i++) {
            float width = CssUtils.parseAbsoluteLength(Float.toString(PdfHtmlResponsiveDesign.pageSizes[i].getWidth()));
            String currentDest = dest.replace("<filename>", "responsive_" + width + ".pdf");
            String currentCmp = cmp.replace("<filename>", "responsive_" + width + ".pdf");

            addError(compareTool.compareByContent(currentDest, currentCmp, outPath, "diff_"));
            addError(compareTool.compareDocumentInfo(currentDest, currentCmp));
        }
    }

    private void unloadLicense() {
        try {
            Field validators = LicenseKey.class.getDeclaredField("validators");
            validators.setAccessible(true);
            validators.set(null, null);
            Field versionField = Version.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(null, null);
        } catch (Exception ignored) {
            // No exception handling required, because there can be no license loaded
        }
    }
}

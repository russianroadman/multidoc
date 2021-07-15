package tanto.multidoc.model;

import com.aspose.pdf.HtmlLoadOptions;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import tanto.multidoc.repos.DocumentRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelUtil {

    public static Version getPreferredVersion(Block b){
        List<Version> versions = b.getVersions();
        for (Version v : versions){
            if (v.isPreferred()) return v;
        }
        return null;
    }

    public static ArrayList<Version> getPreferredVersionsList(Document d){
        ArrayList<Version> list = new ArrayList<>(d.getBlocks().stream().map(Block::getPreferred).collect(Collectors.toList()));
        return list;
    }

    public static ArrayList<Version> getEntireVersionsList(Document d){
        ArrayList<Version> list = new ArrayList<>();
        for (Block block : d.getBlocks()){
            for (Version version : block.getVersions()){
                list.add(version);
            }
        }
        return list;
    }

    public static ArrayList<String> getEntireVersionsListContents(ArrayList<Version> list){
        ArrayList<String> out = new ArrayList<>();
        for (Version version : list){
            out.add(version.getContent());
        }
        return out;
    }

    public static byte[] getPdf(String link, DocumentRepository documentRepository) throws DocumentException, IOException {

        Document doc = documentRepository.findById(link).get();

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, stream);

        BaseFont bf=BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        document.open();
        Font documentTitleFont = new Font(bf, 24, Font.BOLD, BaseColor.BLACK);

        Font blockTitleFont = new Font(bf, 18, Font.BOLD, BaseColor.BLACK);
        Font font = new Font(bf, 14, Font.NORMAL, BaseColor.BLACK);

        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(doc.getTitle(), documentTitleFont));
        document.add(preface);
        document.add( Chunk.NEWLINE );

        for (Block b : doc.getBlocks()){

            preface = new Paragraph();
            preface.add(new Paragraph(b.getTitle(), blockTitleFont));
            document.add(preface);
            document.add( Chunk.NEWLINE );

            preface = new Paragraph();
            preface.add(new Paragraph(b.getPreferred().getContent(), font));
            document.add(preface);
            document.add( Chunk.NEWLINE );

            document.add(new Chunk("by: " + b.getPreferred().getAuthor(), font));
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );

        }

        document.close();

        return stream.toByteArray();

    }

    public static String getDocumentTitle(String link ,DocumentRepository documentRepository) {
        return documentRepository.findById(link).get().getTitle();
    }

    public static byte[] getHtmlToPdf(String link, DocumentRepository documentRepository){

        Document document = documentRepository.findById(link).get();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        ArrayList<String> c = getEntireVersionsListContents(getPreferredVersionsList(document));
        String initialString = "";
        for (String item : c){
            initialString += "<br>" + item;
        }

        InputStream inputStream = new ByteArrayInputStream(initialString.getBytes());

        HtmlLoadOptions htmlLoadOptions = new HtmlLoadOptions();
        com.aspose.pdf.Document doc = new com.aspose.pdf.Document(inputStream, htmlLoadOptions);
        doc.save(stream);

        return stream.toByteArray();
    }

}

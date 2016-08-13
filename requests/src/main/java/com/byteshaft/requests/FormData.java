package com.byteshaft.requests;

import java.util.ArrayList;

public class FormData {

    private static final String CHARSET = "UTF-8";
    private static final String NEW_LINE = "\r\n";
    private static final String DASHES = "--";
    private static final String BOUNDARY_LINE = "---------------------------";
    private static final String SEMICOLON = "; ";
    private ArrayList<MultiPartData> mData;

    public static final String BOUNDARY = BOUNDARY_LINE + System.currentTimeMillis() % 1000;
    public static final int TYPE_CONTENT_TEXT = 1;
    public static final int TYPE_CONTENT_FILE = 2;
    public static String FINISH_LINE = String.format(
            "%s%s%s%s", DASHES, BOUNDARY, DASHES, NEW_LINE
    );

    public FormData() {
        mData = new ArrayList<>();
    }

    private String getContentTypeString(int contentType) {
        switch (contentType) {
            case TYPE_CONTENT_TEXT:
                return String.format("Content-Type: text/plain; charset=%s", CHARSET);
            case TYPE_CONTENT_FILE:
                return "Content-Type: Content-Transfer-Encoding: binary";
            default:
                throw new UnsupportedOperationException("Invalid content type.");
        }
    }

    private String getFieldDispositionLine(int fieldType, String fieldName, String fileName) {
        String simpleDispositionLine = String.format(
                "Content-Disposition: form-data; name=\"%s\"", fieldName
        );
        switch (fieldType) {
            case TYPE_CONTENT_TEXT:
                return simpleDispositionLine;
            case TYPE_CONTENT_FILE:
                String fileNameLine = String.format("filename=\"%s\"", fileName);
                StringBuilder desiredLine = new StringBuilder();
                desiredLine.append(simpleDispositionLine);
                desiredLine.append(SEMICOLON);
                desiredLine.append(fileNameLine);
                return desiredLine.toString();
            default:
                throw new UnsupportedOperationException("Invalid content type.");
        }
    }

    private String getFieldPreContentWriteString(int contentType, String fieldName, String value) {
        StringBuilder partBuilder = new StringBuilder();
        partBuilder.append(DASHES);
        partBuilder.append(BOUNDARY);
        partBuilder.append(NEW_LINE);
        partBuilder.append(getFieldDispositionLine(contentType, fieldName, value));
        partBuilder.append(NEW_LINE);
        partBuilder.append(getContentTypeString(contentType));
        partBuilder.append(NEW_LINE);
        partBuilder.append(NEW_LINE);
        return partBuilder.toString();
    }

    private String getFieldPostContentWriteString(int contentType) {
        switch (contentType) {
            case TYPE_CONTENT_TEXT:
                return NEW_LINE;
            case TYPE_CONTENT_FILE:
                StringBuilder builder = new StringBuilder();
                builder.append(NEW_LINE);
                builder.append(NEW_LINE);
                return builder.toString();
            default:
                throw new UnsupportedOperationException("Invalid content type.");
        }
    }

    public void append(int contentType, String fieldName, String value) {
        MultiPartData data = new MultiPartData();
        data.setContentType(contentType);
        data.setPreContentData(getFieldPreContentWriteString(contentType, fieldName, value));
        data.setContent(value);
        data.setPostContentData(getFieldPostContentWriteString(contentType));
        mData.add(data);
    }

    public ArrayList<MultiPartData> getData() {
        return mData;
    }

    public class MultiPartData {
        private int mContentType;
        private String mPreContentData;
        private String mContent;
        private String mPostContentData;

        public void setContentType(int contentType) {
            mContentType = contentType;
        }

        public int getContentType() {
            return mContentType;
        }

        public void setPreContentData(String preContentData) {
            mPreContentData = preContentData;
        }

        public String getPreContentData() {
            return mPreContentData;
        }

        public void setPostContentData(String postContentData) {
            mPostContentData = postContentData;
        }

        public String getPostContentData() {
            return mPostContentData;
        }

        public void setContent(String content) {
            mContent = content;
        }

        public String getContent() {
            return mContent;
        }
    }
}

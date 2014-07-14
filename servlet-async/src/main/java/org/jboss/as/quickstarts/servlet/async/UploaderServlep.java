package org.jboss.as.quickstarts.servlet.async;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("serial")
@WebServlet(value = "/upload")
public class UploaderServlep extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) {
		System.out.println("Uploadgin");
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.out.println("isMultipart: " + isMultipart);
		if (isMultipart) {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			factory.setRepository(new File("/tmp"));

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			TestProgressListener testProgressListener = new TestProgressListener();
			upload.setProgressListener(testProgressListener);
			
			// Parse the request
			try {
				List<FileItem> items = upload.parseRequest(request);
				System.out.println("Items");
				System.out.println(items.size());
				
				// Process the uploaded items
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (item.isFormField()) {
						processFormField(item);
					} else {
						processUploadedFile(item);
					}
				}

			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void processUploadedFile(FileItem item) throws Exception {
		System.out.println("------ processUploadedFile");
		String fieldName = item.getFieldName();
		String fileName = item.getName();
		String contentType = item.getContentType();
		boolean isInMemory = item.isInMemory();
		long sizeInBytes = item.getSize();

		System.out.println(fieldName);
		System.out.println(fileName);
		System.out.println(contentType);
		System.out.println(isInMemory);
		System.out.println(sizeInBytes);
		
		String filePath = "/tmp/" + fileName;
		File storeFile = new File(filePath);
		
		// saves the file on disk
		System.out.println("Writting to " + storeFile.getAbsolutePath());
		item.write(storeFile);

	}

	private void processFormField(FileItem item) throws Exception {
		System.out.println("------ processFormField");
		System.out.println(item.getName());
		System.out.println(item.getString());
		
		
	}
}
